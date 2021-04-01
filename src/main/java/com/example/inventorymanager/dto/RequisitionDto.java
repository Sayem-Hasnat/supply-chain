package com.example.inventorymanager.dto;

import com.example.inventorymanager.entity.Wishlist;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequisitionDto {
    private long requisitionId;
    private String requisitionNo;
    private List<Wishlist> productList;

    private double payable;
    private double paid;
    private String paymentStatus;
    private String status;
    private String dealer;


}
