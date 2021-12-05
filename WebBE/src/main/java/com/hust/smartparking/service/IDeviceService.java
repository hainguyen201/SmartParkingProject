package com.hust.smartparking.service;

import com.hust.smartparking.entity.Device;
import com.hust.smartparking.entity.User;

public interface IDeviceService extends IGeneralService<Device>{
    Iterable<Device> searchData(Device device);
}
