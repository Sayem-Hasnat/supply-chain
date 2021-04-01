package com.example.inventorymanager.service;

import com.example.inventorymanager.entity.Apply;
import com.example.inventorymanager.repository.ApplyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    ApplyRepo applyRepo;


    public List<Apply> getAllApplyList() {
    return applyRepo.findAllByStatus("pending");
    }

    public Apply getApplyDetails(long applyId) {
        return applyRepo.getOne(applyId);
    }
}
