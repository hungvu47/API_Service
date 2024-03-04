package com.apphotel.repository;

import com.apphotel.model.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TypeRoomRepository extends JpaRepository<TypeRoom, Long> {
    boolean existsByName(String name);
}
