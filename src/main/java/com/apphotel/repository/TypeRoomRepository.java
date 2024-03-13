package com.apphotel.repository;

import com.apphotel.model.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TypeRoomRepository extends JpaRepository<TypeRoom, Long> {
    boolean existsByName(String name);

    Optional<TypeRoom> findById(Long typeRoomId);
}
