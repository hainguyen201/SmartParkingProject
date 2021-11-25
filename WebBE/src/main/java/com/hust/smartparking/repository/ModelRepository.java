package com.hust.smartparking.repository;

import com.hust.smartparking.entity.Model;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ModelRepository extends CrudRepository<Model, Integer> {
    @Modifying
    @Query(value= "update  models set m.status=1 where m.id=:id", nativeQuery = true)
    void updateModelStatus(@Param("id") int id);

    @Modifying
    @Query(value = "update models set status=0 where id!=:id", nativeQuery = true)
    void resetModelStatus(@Param("id")  int id);
}
