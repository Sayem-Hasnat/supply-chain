package com.example.inventorymanager.repository;

import com.example.inventorymanager.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    @Query
    Transaction findByTransactionNo(String transactionNo);
}
