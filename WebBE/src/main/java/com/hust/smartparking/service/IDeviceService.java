package com.hust.smartparking.service;

import com.hust.smartparking.entity.Device;

public interface IDeviceService extends IGeneralService<Device>{
    Iterable<Device> searchData(Device device);
    Iterable<Device> findByGateId(Long id);
}
