package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.AppParam;
import com.hust.smartparking.repository.AppParamRepository;
import com.hust.smartparking.service.IAppParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AppParamService implements IAppParamService {
    @Autowired private AppParamRepository appParamRepository;
    @Override
    public Iterable<AppParam> findAll() {
        return appParamRepository.findAll();
    }

    @Override
    public Optional<AppParam> findById(int id) {
        return appParamRepository.findById(id);
    }

    @Override
    public AppParam save(AppParam appParam) {
        return appParamRepository.save(appParam);
    }

    @Override
    public void remove(int id) {
        appParamRepository.deleteById(id);
    }
}
