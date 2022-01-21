package com.hust.smartparking.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "devices")
@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Long type;

    @Column(name = "status")
    private Long status;

    @Column(name = "address")
    private String address;


    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "modified_date")
    private Timestamp modifiedDate;

    @Column(name = "position_id")
    private Long positionId;
    @Column(name = "gate_id")
    private Long gateId;
}