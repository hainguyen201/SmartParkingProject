package com.hust.smartparking.repository;

import com.hust.smartparking.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
