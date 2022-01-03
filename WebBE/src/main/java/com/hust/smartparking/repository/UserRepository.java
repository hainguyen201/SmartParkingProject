package com.hust.smartparking.repository;

import com.hust.smartparking.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "select * from users where username =?1", nativeQuery = true)
    public Iterable<User> findByUsername(String username);
}
