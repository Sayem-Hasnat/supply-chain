package com.example.inventorymanager.service;

import com.example.inventorymanager.entity.Category;
import com.example.inventorymanager.entity.Item;
import com.example.inventorymanager.entity.Requisition;
import com.example.inventorymanager.repository.CategoryRepo;
import com.example.inventorymanager.repository.ItemRepo;
import com.example.inventorymanager.repository.RequisitionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    ItemRepo itemRepo;
    @Autowired
    RequisitionRepo requisitionRepo;

    public void saveCategory(Category category) {
        categoryRepo.save(category);
    }

    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    public String saveItem(Item item) {
        if (itemRepo.findByItemName(item.getItemName()) != null) {
            return "Product Already Exist";
        } else {
            itemRepo.save(item);
            return String.valueOf(itemRepo.findByItemName(item.getItemName()).getItemId());
        }
    }

    public Category getCategory(long categoryId) {
        return categoryRepo.getOne(categoryId);
    }

    public Item getItemDetails(long itemId) {
        return itemRepo.getOne(itemId);
    }

    public int updateItemStockQuantity(Item item) {
        Item item1 = itemRepo.getOne(item.getItemId());
        item1.setItemQuantity(item1.getItemQuantity() + item.getItemQuantity());
        itemRepo.save(item1);
        return item1.getItemQuantity();
    }

    public Requisition getRequisitionDetails(long requisitionId) {
    return requisitionRepo.getOne(requisitionId);
    }

    public void sendRequisitionToAccountManager(long requisitionId) {
       Requisition requisition= this.getRequisitionDetails(requisitionId);
       requisition.setStatus("Send To Account Manager");
       requisitionRepo.save(requisition);
    }

    public void sendRequisitionToDeliveryIncharge(long requisitionId) {
        Requisition requisition= this.getRequisitionDetails(requisitionId);
        requisition.setStatus("Send To Delivery Incharge");
        requisitionRepo.save(requisition);
    }
}
