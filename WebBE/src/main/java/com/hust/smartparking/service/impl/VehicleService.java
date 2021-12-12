package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.Device;
import com.hust.smartparking.entity.Vehicle;
import com.hust.smartparking.repository.VehicleRepository;
import com.hust.smartparking.service.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService implements IVehicleService {
    @Autowired private VehicleRepository vehicleRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Iterable<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public void remove(int id) {
        vehicleRepository.deleteById(id);
    }
    public Iterable<Vehicle> searchData(Vehicle vehicle, Timestamp fromEntrance, Timestamp toEntrance,
                                        Timestamp fromExit, Timestamp toExit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vehicle> query=cb.createQuery(Vehicle.class);
        Root<Vehicle> vehicleRoot=query.from(Vehicle.class);
        Path<String> licenseNumber=vehicleRoot.get("licenseNumber");
        Path<Timestamp> entranceTime=vehicleRoot.get("entranceTime");
        Path<Timestamp> exitTime=vehicleRoot.get("exitTime");
        Path<Long> type=vehicleRoot.get("type");
        List<Predicate> predicateList=new ArrayList<>();
        if(vehicle.getLicenseNumber()!=null && !vehicle.getLicenseNumber().isEmpty()){
            predicateList.add(cb.like(licenseNumber, "%"+vehicle.getLicenseNumber()+"%"));
        }
        if(vehicle.getType()!=null){
            predicateList.add(cb.equal(type, vehicle.getType()));
        }
        if(fromEntrance!=null || toEntrance !=null){
            List<Predicate> list= new ArrayList<>();

            if(fromEntrance!=null && toEntrance !=null) {
                list.add(cb.greaterThanOrEqualTo(entranceTime, fromEntrance));
                list.add(cb.lessThanOrEqualTo(entranceTime, toEntrance));
                predicateList.add(cb.and(list.toArray(new Predicate[list.size()])));
            }
            else if(fromEntrance !=null){
                predicateList.add(cb.greaterThanOrEqualTo(entranceTime, fromEntrance));
            }
            else if(toEntrance !=null){
                predicateList.add(cb.lessThanOrEqualTo(entranceTime, toEntrance));
            }

        }
        if(fromExit!=null || toExit!=null){
            List<Predicate> list=new ArrayList<>();
            if(fromExit!=null && toExit !=null) {
                list.add(cb.greaterThanOrEqualTo(exitTime, fromExit));
                list.add(cb.lessThanOrEqualTo(exitTime, toExit));
                predicateList.add(cb.and(list.toArray(new Predicate[list.size()])));
            }
            else if(fromExit !=null){
                predicateList.add(cb.greaterThanOrEqualTo(exitTime, fromExit));
            }
            else if(toExit !=null){
                predicateList.add(cb.lessThanOrEqualTo(exitTime, toExit));
            }

        }
        query.select(vehicleRoot).where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
        return entityManager.createQuery(query).getResultList();
    }
}
