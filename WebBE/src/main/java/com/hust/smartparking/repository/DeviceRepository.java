package com.hust.smartparking.repository;

import com.hust.smartparking.entity.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DeviceRepository extends CrudRepository<Device, Integer> {
    @Query(value = "select * from devices where type=3", nativeQuery = true)
    Iterable<Device> getCamera();
    @Query(value = "select * from devices where gate_id = ?1", nativeQuery = true)
    Iterable<Device> findByGateId(Long id);
}
