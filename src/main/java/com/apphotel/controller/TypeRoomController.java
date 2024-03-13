package com.apphotel.controller;

import com.apphotel.exception.UserException;
import com.apphotel.model.TypeRoom;
import com.apphotel.response.TypeRoomResponse;
import com.apphotel.service.ITypeRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomtype")
public class TypeRoomController {

    @Autowired
    private ITypeRoomService typeRoomService;

    @GetMapping("/all-room-type")
    public ResponseEntity<List<TypeRoom>> getAllTypeRooms()  {
        List<TypeRoom> roomTypes = typeRoomService.getAllTypeRooms();
        return new ResponseEntity<>(roomTypes, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TypeRoomResponse> addTypeRoom(@RequestBody TypeRoom typeRoom) {
        try {
            TypeRoom addedTypeRoom = typeRoomService.addTypeRoom(typeRoom);

            TypeRoomResponse response = new TypeRoomResponse(addedTypeRoom.getTypeRoomId(), addedTypeRoom.getName());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new TypeRoomResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update-type-room/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TypeRoomResponse> updateTypeRoom(@PathVariable Long id,
                                                           @RequestBody TypeRoom typeRoom) {
        try {
            TypeRoom updatedTypeRoom = typeRoomService.updateTypeRoom(id, typeRoom);
            TypeRoomResponse response = new TypeRoomResponse(updatedTypeRoom.getTypeRoomId(), updatedTypeRoom.getName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(new TypeRoomResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-type-room/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTypeRoom(@PathVariable Long id) {
        typeRoomService.deleteTypeRoom(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
