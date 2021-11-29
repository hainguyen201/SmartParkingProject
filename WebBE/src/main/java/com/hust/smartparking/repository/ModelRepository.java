package com.hust.smartparking.repository;

import com.hust.smartparking.entity.Model;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ModelRepository extends CrudRepository<Model, Integer> {
    @Modifying
    @Transactional
    @Query(value= "update models set status=1 where id=?1", nativeQuery = true)
    void updateModelStatus(int id);

    @Modifying
    @Transactional
    @Query(value = "update models set status=0 where id!=?1", nativeQuery = true)
    void resetModelStatus(int id);
}
