package com.hust.smartparking.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Table(name = "car_parking_areas")
@Entity
public class ParkingArea {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "modified_date")
    private Timestamp modifiedDate;
    @Column(name = "num_in_used")
    private int numInUsed;
    @Column(name = "max_number")
    private int maxNumber;

}
