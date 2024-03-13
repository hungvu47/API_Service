package com.apphotel.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import com.apphotel.model.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.apphotel.exception.ImageRetrievalException;
import com.apphotel.exception.ResourceNotFoundException;
import com.apphotel.model.BookedRoom;
import com.apphotel.model.Room;
import com.apphotel.response.BookingResponse;
import com.apphotel.response.RoomResponse;
import com.apphotel.service.IRoomService;
import com.apphotel.service.impl.BookingServiceImpl;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

	@Autowired
	private IRoomService roomService;

	@Autowired
	private BookingServiceImpl bookingService;

	@PostMapping("/addNewRoom")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<RoomResponse> addNewRooma(@RequestParam("image") MultipartFile image
													, @RequestParam("roomPrice") BigDecimal roomPrice
													,@RequestParam("roomName") String roomName
													,@RequestParam("roomNumber") String roomNumber
													,@RequestParam("description") String description
													, @RequestParam("typeRoomId") Long typeRoomId) {

		Supplier<ResponseEntity<RoomResponse>> addNewRoomSupplier = () -> {
			try {
				Room savedRoom = roomService.addNewRoom(image, roomPrice,roomName,roomNumber,description, typeRoomId);
				RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomPrice(),
						savedRoom.getRoomName(), savedRoom.getRoomNumber(), savedRoom.getDescription(),
						savedRoom.getTypeRoom());
				return ResponseEntity.ok(response);
			} catch (SQLException | IOException e) {
				// Handle exceptions as needed
				return ResponseEntity.status(500).build(); // For example, return a 500 Internal Server Error
			}
		};

		return addNewRoomSupplier.get();
	}

	@GetMapping("/all-rooms")
	public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
		List<Room> rooms = roomService.getAllRooms();
		List<RoomResponse> roomResponses = new ArrayList<>();
		for (Room room : rooms) {
			byte[] photoBytes = roomService.getRoomImageById(room.getId());
			if (photoBytes != null && photoBytes.length > 0) {
				String base64Photo = Base64.encodeBase64String(photoBytes);
				RoomResponse roomResponse = getRoomResponse(room);
				roomResponse.setImage(base64Photo);
				roomResponses.add(roomResponse);
			}
		}
		return ResponseEntity.ok(roomResponses);
	}

	@DeleteMapping("/delete-room/{roomId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
		roomService.deleteRoom(roomId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/update/{roomId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
												   @RequestParam(required = false) BigDecimal roomPrice,
												   @RequestParam(required = false) String roomName,
												   @RequestParam(required = false) String roomNumber,
												   @RequestParam(required = false) String description,
												   @RequestParam(required = false) Long typeRoomId,
												   @RequestParam(required = false) MultipartFile image)
			throws IOException, SerialException, SQLException {

		byte[] photoBytes = image != null && !image.isEmpty() ? image.getBytes() : roomService.getRoomImageById(roomId);
		Blob blob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
		Room theRoom = roomService.updateRoom(roomId, photoBytes, roomPrice, roomName,
				roomNumber, description, typeRoomId);
		theRoom.setImage(blob);
		RoomResponse response = getRoomResponse(theRoom);

		return ResponseEntity.ok(response);

	}

	@GetMapping("/room/{roomId}")
	public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
		Optional<Room> roomOptional = roomService.getRoomById(roomId);
		return roomOptional.map(room -> {
			RoomResponse response = getRoomResponse(room);
			return ResponseEntity.ok(Optional.of(response));
		}).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
	}
	
//	@GetMapping("/available-rooms")
//	public ResponseEntity<List<RoomResponse>> getRoomAvailable(
//	        @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
//	        @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
//	        @RequestParam("roomType") String roomType) {
//	    List<Room> availableRoom = roomService.getAvailableRooms(checkIn, checkOut, roomType);
//
//	    List<RoomResponse> roomResponses = availableRoom.stream()
//	            .map(room -> {
//	                byte[] photoBytes = roomService.getRoomImageById(room.getId());
//	                if (photoBytes != null && photoBytes.length > 0) {
//	                    String imageBase64 = Base64.encodeBase64String(photoBytes);
//	                    RoomResponse roomResponse = getRoomResponse(room);
//	                    roomResponse.setImage(imageBase64);
//	                    return roomResponse;
//	                }
//	                return null;
//	            })
//	            .filter(Objects::nonNull)
//	            .collect(Collectors.toList());
//
//	    if (roomResponses.isEmpty()) {
//	        return ResponseEntity.noContent().build();
//	    } else {
//	        return ResponseEntity.ok(roomResponses);
//	    }
//	}


	private RoomResponse getRoomResponse(Room room) {
		List<BookedRoom> bookedRooms = getAllBookingByRoomId(room.getId());
		List<BookingResponse> bookingResponses = bookedRooms.stream()
				.map(booking -> new BookingResponse(booking.getId(), booking.getCheckIn(), booking.getCheckOut(),
						booking.getConfirmCode()))
				.toList();
		byte[] imageBytes = null;
		Blob blobImage = room.getImage();
		if (blobImage != null) {
			try {
				imageBytes = blobImage.getBytes(1, (int) blobImage.length());
			} catch (SQLException e) {
				throw new ImageRetrievalException("Error get image response");
			}
		}
		return new RoomResponse(room.getId(), room.getRoomPrice(), room.getRoomName(), room.isBooked(), imageBytes,
				 room.getRoomNumber(), room.getDescription(), room.getTypeRoom().getTypeRoomId(),
				room.getTypeRoom().getName(), bookingResponses);
	}

	private List<BookedRoom> getAllBookingByRoomId(Long roomId) {

		return bookingService.getAllBookingByRoomId(roomId);
	}
}
