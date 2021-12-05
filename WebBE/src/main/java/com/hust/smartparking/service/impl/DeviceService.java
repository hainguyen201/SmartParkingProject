package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.Device;
import com.hust.smartparking.entity.User;
import com.hust.smartparking.repository.DeviceRepository;
import com.hust.smartparking.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService implements IDeviceService {
    @Autowired private DeviceRepository deviceRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Iterable<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Optional<Device> findById(int id) {
        return deviceRepository.findById(id);
    }

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public void remove(int id) {
        deviceRepository.deleteById(id);
    }


    @Override
    public Iterable<Device> searchData(Device device) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Device> query=cb.createQuery(Device.class);
        Root<Device> devices=query.from(Device.class);
        Path<String> name=devices.get("name");
        Path<Long> type=devices.get("type");
        Path<Long> status=devices.get("status");
        Path<String> address=devices.get("address");
        Path<Long> positionId=devices.get("positionId");
        List<Predicate> predicateList=new ArrayList<>();
        if(device.getName()!=null && !device.getName().isEmpty())
            predicateList.add(cb.like(name, "%"+device.getName()+"%"));
        if(device.getType()!=null){
            predicateList.add(cb.equal(type, device.getType()));
        }
        if(device.getStatus()!=null){
            predicateList.add(cb.equal(status, device.getStatus()));
        }
        if(device.getAddress()!=null && !device.getAddress().isEmpty()){
            predicateList.add(cb.like(address, "%"+device.getAddress()+"%"));
        }
        if(device.getPositionId()!=null){
            predicateList.add(cb.equal(positionId, device.getPositionId()));
        }
        query.select(devices).where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
        return entityManager.createQuery(query).getResultList();
    }
}
