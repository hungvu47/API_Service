package com.apphotel.service;

import com.apphotel.model.TypeRoom;

import java.util.List;


public interface ITypeRoomService {
    List<TypeRoom> getAllTypeRooms();

    TypeRoom addTypeRoom(TypeRoom typeRoom);

    TypeRoom updateTypeRoom(Long id, TypeRoom typeRoom);

    void deleteTypeRoom(Long id);
}
