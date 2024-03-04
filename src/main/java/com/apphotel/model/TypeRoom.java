package com.apphotel.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "type_room")
@Data
public class TypeRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_room_id")
    private Long id;

    @Column(name = "type_room_name")
    private String name;

    @OneToMany(mappedBy = "typeRoom")
    private List<Room> rooms;
}
