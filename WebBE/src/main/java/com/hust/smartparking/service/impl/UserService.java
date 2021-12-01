package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.User;
import com.hust.smartparking.repository.UserRepository;
import com.hust.smartparking.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired private UserRepository userRepo;

    @Override
    public Iterable<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepo.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public void remove(int id) {
        userRepo.deleteById(id);
    }
}
