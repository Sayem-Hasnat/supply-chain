package com.example.inventorymanager.repository;

import com.example.inventorymanager.entity.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequisitionRepo extends JpaRepository<Requisition,Long> {
    @Query
    Requisition findByPaymentStatusAndUser_UserId(String paymentStatus,long userId);

    @Query
    List<Requisition> findByUser_UserId(long userId);

    @Query
    List<Requisition> findAllByStatus(String status);

}
