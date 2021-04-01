package com.example.inventorymanager.service;

import com.example.inventorymanager.entity.Requisition;
import com.example.inventorymanager.repository.RequisitionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountManagerService {
    @Autowired
    RequisitionRepo requisitionRepo;


    public void sendRequisitionToInventoryManager(long requisitionId) {
        Requisition requisition= this.getRequisitionDetails(requisitionId);
        requisition.setStatus("Ready To Delivery");
        requisitionRepo.save(requisition);
    }
    public Requisition getRequisitionDetails(long requisitionId) {
        return requisitionRepo.getOne(requisitionId);
    }
}
