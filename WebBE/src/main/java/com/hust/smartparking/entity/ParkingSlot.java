package com.hust.smartparking.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "parking_slots")
@Entity
public class ParkingSlot {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "type")
    private int type;
    @Column(name = "status")
    private int status;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name="modified_date")
    private Timestamp modifiedDate;
    @Column(name = "parking_area_id")
    private Long parkingAreaId;

}
