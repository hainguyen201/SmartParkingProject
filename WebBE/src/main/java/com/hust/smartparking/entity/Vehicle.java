package com.hust.smartparking.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "vehicles", schema = "smartparking")
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name = "type")
    private  int type;
    @Column(name = "license_number")
    private  String licenseNumber;
    @Column(name = "entrance_image")
    private byte[] entranceImage;
    @Column(name = "exit_image")
    private byte[] exitImage;
    @Column(name = "entrance_time")
    private Timestamp entranceTime;
    @Column(name = "exit_time")
    private Timestamp exitTime;
}