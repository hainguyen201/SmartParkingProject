package com.hust.smartparking.service;

import com.hust.smartparking.entity.User;

public interface IUserService extends IGeneralService<User>{
    Iterable<User> searchData(User user);
}
