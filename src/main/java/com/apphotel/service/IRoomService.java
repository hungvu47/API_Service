package com.apphotel.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.apphotel.response.RoomResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.apphotel.model.Room;

public interface IRoomService {

	Room addNewRoom(MultipartFile image, BigDecimal roomPrice, String roomName,
					String roomNumber, String description, Long typeRoomId) throws SQLException, IOException;

//	List<String> getAllRoomTypes();

	List<Room> getAllRooms();

	byte[] getRoomImageById(Long roomId);

	void deleteRoom(Long roomId);

	Room updateRoom(Long roomId,byte[] photoBytes, BigDecimal roomPrice, String roomName,
					String roomNumber, String description, Long typeRoomId);

	Optional<Room> getRoomById(Long roomId);

//	List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, String roomType);
}
