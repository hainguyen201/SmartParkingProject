package com.hust.smartparking.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "gates")
@Entity
public class Gate {
    @Id
    @Column(name = "gate_id", nullable = false)
    private int gateId;
    @Column(name = "gate_name")
    private String gateName;
    @Column(name = "type")
    private int type;
    @Column(name = "vehicle_type")
    private int vehicleType;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "modified_date")
    private Timestamp modifiedDate;

}
