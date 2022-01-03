package com.hust.smartparking.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Table(name = "app_params", schema = "smartparking")
@Entity
public class AppParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "tablename")
    private String tablename;
    @Column(name = "param")
    private int param;
    @Column(name = "description")
    private  String description;
}
