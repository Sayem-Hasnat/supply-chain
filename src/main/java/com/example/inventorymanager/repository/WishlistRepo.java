package com.example.inventorymanager.repository;

import com.example.inventorymanager.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist,Long> {
    List<Wishlist> findAllByEnableTrueAndUser_UserId(long userId);
}
