package com.apphotel.model;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.apache.commons.lang3.RandomStringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
	private Long id;
	private BigDecimal roomPrice;
//	private boolean isBooked = false;
	private String roomName;
	private String description;
	
	@Lob
	private Blob image;

	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<BookedRoom> bookedRooms = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "type_id", nullable = false)
	private TypeRoom typeRoom;

	public void addBooking(BookedRoom booking) {
		if (bookedRooms == null) {
			bookedRooms = new ArrayList<>();
		}
		bookedRooms.add(booking);
		booking.setRoom(this);
//		isBooked = true;
		String bookingCode = RandomStringUtils.randomNumeric(10);
		booking.setConfirmCode(bookingCode);
	}
}
