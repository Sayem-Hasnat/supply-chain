package com.example.inventorymanager.dto;

import com.example.inventorymanager.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistDto {

    private long wishlistId;
    private Item item;
    private int orderQuantity;
    private double orderQuantityPrice;

    private String itemName;
}
