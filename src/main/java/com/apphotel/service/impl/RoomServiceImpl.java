package com.apphotel.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import com.apphotel.exception.InternalServerException;
import com.apphotel.model.TypeRoom;
import com.apphotel.repository.TypeRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.apphotel.exception.ResourceNotFoundException;
import com.apphotel.model.Room;
import com.apphotel.repository.RoomRepository;
import com.apphotel.service.IRoomService;


@Service
public class RoomServiceImpl implements IRoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private TypeRoomRepository typeRoomRepository;


	@Override
	public Room addNewRoom(MultipartFile imageFile, BigDecimal roomPrice, String roomName,
						   String roomNumber, String description, Long typeRoomId)
			throws SQLException, IOException {
		TypeRoom roomType = typeRoomRepository.findById(typeRoomId)
				.orElseThrow(() -> new RuntimeException("RoomType not found"));
		Room room = new Room();
		room.setRoomPrice(roomPrice);
		room.setRoomName(roomName);
		room.setRoomNumber(roomNumber);
		room.setDescription(description);
		if (!imageFile.isEmpty()) {
			byte[] imageBytes = imageFile.getBytes();
			Blob imageBlob = new SerialBlob(imageBytes);
			room.setImage(imageBlob);
		}
		room.setTypeRoom(roomType);
		return roomRepository.save(room);
	}

	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	public byte[] getRoomImageById(Long roomId) {
		Optional<Room> optional = roomRepository.findById(roomId);
		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("Room not found with ID " + roomId);
		}
		Blob imageBlob = optional.get().getImage();
		if (imageBlob != null) {
			try {
				return imageBlob.getBytes(1, (int) imageBlob.length());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void deleteRoom(Long roomId) {
		Optional<Room> optional = roomRepository.findById(roomId);
		if (optional.isPresent()) {
			roomRepository.deleteById(roomId);
		}

	}

	@Override
	public Room updateRoom(Long roomId, byte[] photoBytes, BigDecimal roomPrice, String roomName,
						   String roomNumber, String description, Long typeRoomId) {
		Room existingRoom = roomRepository.findById(roomId)
				.orElseThrow(() -> new ResourceNotFoundException("Room not found"));

		existingRoom.setRoomPrice(roomPrice);
		existingRoom.setRoomName(roomName);
		existingRoom.setRoomNumber(roomNumber);
		existingRoom.setDescription(description);
		if(photoBytes != null && photoBytes.length > 0) {
			try {
				existingRoom.setImage(new SerialBlob(photoBytes));
			} catch (SQLException e) {
				throw new InternalServerException("Error updating room");
			}
		}
		TypeRoom typeRoom = new TypeRoom();
		typeRoom.setTypeRoomId(typeRoomId);
		existingRoom.setTypeRoom(typeRoom);
		return roomRepository.save(existingRoom);
	}

	@Override
	public Optional<Room> getRoomById(Long roomId) {
		return Optional.of(roomRepository.findById(roomId).get());
	}

//	@Override
//	public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, String roomType) {
//		return roomRepository.findAvailableRooms(checkIn, checkOut, roomType);
//	}

}
