package com.apphotel.response;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;

import com.apphotel.model.TypeRoom;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
	private Long id;
	private BigDecimal roomPrice;
	private String roomName;
	private boolean isBooked;
	private String image;
	private String roomNumber;
	private List<BookingResponse> bookings;
	private  String description;
	private TypeRoom typeRoom;

	public RoomResponse(Long id, BigDecimal roomPrice, String roomName, String roomNumber, String description, TypeRoom typeRoom) {
		super();
		this.id = id;
		this.roomPrice = roomPrice;
		this.roomName = roomName;
		this.roomNumber = roomNumber;
		this.description = description;
		this.typeRoom = typeRoom;
	}

	public RoomResponse(Long id, BigDecimal roomPrice, String roomName, boolean isBooked, byte[] imageBytes,
						String roomNumber, String description,Long typeRoomId, String name, List<BookingResponse> bookings) {
		super();
		this.id = id;
		this.roomPrice = roomPrice;
		this.roomName = roomName;
		this.isBooked = isBooked;
		this.image = imageBytes != null ? Base64.encodeBase64String(imageBytes) : null;
		this.roomNumber = roomNumber;
		this.description = description;
		this.typeRoom = new TypeRoom();
		this.typeRoom.setTypeRoomId(typeRoomId);
		this.typeRoom.setName(name);
		this.bookings = bookings;
	}
}