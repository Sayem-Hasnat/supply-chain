package com.example.inventorymanager.repository;

import com.example.inventorymanager.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRepo extends JpaRepository<Apply,Long> {
    @Query
    Apply findByUsername(String username);

    @Query
    List<Apply> findAllByStatus(String s);
}
