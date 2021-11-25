package com.hust.smartparking.service;

import com.hust.smartparking.entity.Model;

public interface IModelService extends IGeneralService<Model>{
    public void updateModelStatus(int id);
}
