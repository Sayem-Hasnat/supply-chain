package com.example.inventorymanager.repository;

import com.example.inventorymanager.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends JpaRepository<Item,Long> {
    @Query
    Item findByItemName(String itemName);
}
