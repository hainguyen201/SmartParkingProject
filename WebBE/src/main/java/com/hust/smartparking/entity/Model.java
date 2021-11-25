package com.hust.smartparking.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "models", schema = "smartparking")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "url")
    private String url;
    @Column(name="created_date")
    private Timestamp createdDate;
    @Column(name="modified_date")
    private Timestamp modifiedDate;
    @Column(name = "status")
    private int status;
}
