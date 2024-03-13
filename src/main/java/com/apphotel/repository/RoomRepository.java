package com.apphotel.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apphotel.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByRoomNumber(String roomNumber);

//	@Query("SELECT DISTINCT r.typeRoom FROM Room r")
//	List<String> findRoomTypes();
//
//	   @Query(" SELECT r FROM Room r " +
//	            " WHERE r.typeRoom LIKE %:roomType% " +
//	            " AND r.id NOT IN (" +
//	            "  SELECT br.room.id FROM BookedRoom br " +
//	            "  WHERE ((br.checkIn <= :checkOut) AND (br.checkOut >= :checkIn))" +
//	            ")")
//	    List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, String roomType);

}
