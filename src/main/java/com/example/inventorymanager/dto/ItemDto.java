package com.example.inventorymanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ItemDto implements Serializable {
    private long itemId;
    private String itemName;
    private String itemImage;
    private String itemImagePath;
    private int itemPerCarton;
    private double pricePerCarton;
    private int itemQuantity;

    private long categoryId;

}
