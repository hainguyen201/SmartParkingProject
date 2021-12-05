package com.hust.smartparking.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Table(name = "app_params")
@Entity
public class AppParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "table")
    private String table;
    @Column(name = "param")
    private int param;
    @Column(name = "description")
    private  String description;
}
