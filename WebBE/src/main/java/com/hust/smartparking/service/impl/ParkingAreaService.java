package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.Device;
import com.hust.smartparking.entity.ParkingArea;
import com.hust.smartparking.entity.ParkingSlot;
import com.hust.smartparking.repository.ParkingAreaRepository;
import com.hust.smartparking.repository.ParkingSlotRepository;
import com.hust.smartparking.service.IParkingAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ParkingAreaService implements IParkingAreaService {
    @Autowired private ParkingAreaRepository parkingAreaRepository;
    @Autowired private ParkingSlotRepository parkingSlotRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Iterable<ParkingArea> findAll() {
        return parkingAreaRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Optional<ParkingArea> findById(int id) {
        return parkingAreaRepository.findById(id);
    }

    @Override
    public ParkingArea save(ParkingArea parkingArea) {
        return parkingAreaRepository.save(parkingArea);
    }

    @Override
    public void remove(int id) {
        parkingAreaRepository.deleteById(id);
    }

    @Override
    public Iterable<ParkingArea> searchData(ParkingArea parkingArea) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ParkingArea> query=cb.createQuery(ParkingArea.class);
        Root<ParkingArea> parkingAreas=query.from(ParkingArea.class);
        Path<String> name=parkingAreas.get("name");
        Path<Long> id=parkingAreas.get("id");
        List<Predicate> predicateList=new ArrayList<>();
        if(parkingArea.getName()!=null && !parkingArea.getName().isEmpty())
            predicateList.add(cb.like(name, "%"+parkingArea.getName()+"%"));
        if(parkingArea.getId()!=0){
            predicateList.add(cb.equal(id, parkingArea.getId()));
        }

        query.select(parkingAreas).where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public ParkingArea findByParkingSlot(int id) {
        try{
            ParkingSlot parkingSlot= parkingSlotRepository.findById(id).get();
            ParkingArea  parkingArea= parkingAreaRepository.findById(Math.toIntExact(parkingSlot.getParkingAreaId())).get();
            return parkingArea;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }



    }

}
