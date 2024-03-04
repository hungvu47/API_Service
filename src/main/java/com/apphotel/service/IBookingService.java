package com.apphotel.service;

import java.util.List;

import com.apphotel.model.BookedRoom;

public interface IBookingService {

	void cancelBooking(Long bookingId);

	String saveBooking(Long roomId, BookedRoom bookedReq);

	BookedRoom findByConfirmCode(String confirmCode);

	List<BookedRoom> getAllBookings();

	List<BookedRoom> getBookingsByUserEmail(String email);

}
