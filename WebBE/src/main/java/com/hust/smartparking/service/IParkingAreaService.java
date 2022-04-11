package com.hust.smartparking.service;

import com.hust.smartparking.entity.ParkingArea;
import com.hust.smartparking.entity.User;

public interface IParkingAreaService extends IGeneralService<ParkingArea>{
    Iterable<ParkingArea> searchData(ParkingArea parkingArea);
    ParkingArea findByParkingSlot(int id);
}
