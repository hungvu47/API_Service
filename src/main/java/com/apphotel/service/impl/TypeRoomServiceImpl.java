package com.apphotel.service.impl;

import com.apphotel.model.TypeRoom;
import com.apphotel.repository.TypeRoomRepository;
import com.apphotel.service.ITypeRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeRoomServiceImpl implements ITypeRoomService {

    @Autowired
    private TypeRoomRepository typeRoomRepository;

    @Override
    public List<TypeRoom> getAllTypeRooms() {
        return typeRoomRepository.findAll();
    }

    @Override
    public TypeRoom addTypeRoom(TypeRoom typeRoom) {
        if(typeRoomRepository.existsByName(typeRoom.getName())) {
            throw  new RuntimeException("Loại phòng đã tồn tại");
        }
        return typeRoomRepository.save(typeRoom);
    }

    @Override
    public TypeRoom updateTypeRoom(Long id, TypeRoom typeRoom) {
        TypeRoom existingTypeRoom = typeRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phòng"));

        if(!existingTypeRoom.getName().equals(typeRoom.getName())) {
            if(typeRoomRepository.existsByName(typeRoom.getName())) {
                throw new RuntimeException("Loại phòng này đã tồn tại");
            }
        }

        existingTypeRoom.setName(typeRoom.getName());
        return typeRoomRepository.save(existingTypeRoom);
    }

    @Override
    public void deleteTypeRoom(Long id) {
        Optional<TypeRoom> optional = typeRoomRepository.findById(id);
        if (optional.isPresent()) {
            typeRoomRepository.deleteById(id);
        }
    }
}
