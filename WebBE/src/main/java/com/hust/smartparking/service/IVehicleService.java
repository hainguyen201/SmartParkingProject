package com.hust.smartparking.service;

import com.hust.smartparking.entity.Vehicle;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;

public interface IVehicleService  extends IGeneralService<Vehicle>{
    Iterable<Vehicle> searchData(Vehicle vehicle, Timestamp fromEntrance, Timestamp toEntrance,
                                        Timestamp fromExit, Timestamp toExit);
}
