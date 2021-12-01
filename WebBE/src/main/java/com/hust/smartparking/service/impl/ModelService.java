package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.Model;
import com.hust.smartparking.repository.ModelRepository;
import com.hust.smartparking.service.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ModelService implements IModelService {
    @Autowired private ModelRepository modelRepository;
    @Override
    public Iterable<Model> findAll(){
        return modelRepository.findAll();
    }
    @Override
    public Optional<Model> findById(int id){
        return modelRepository.findById(id);
    }
    @Override
    public Model save(Model model){
        return modelRepository.save(model);
    }

    @Override
    public void remove(int id) {
        modelRepository.deleteById(id);
    }
    @Override
    public void updateModelStatus(int id){
        modelRepository.updateModelStatus(id);
        modelRepository.resetModelStatus(id);
    }

}
