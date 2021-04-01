package com.example.inventorymanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class TransactionDto {
    private long transactionId;
    private String transactionNo;
    private double amount;
    private String account;
    private String date;
    private String time;
    private String requisitionNo;

    public TransactionDto() {
        UUID uuid = UUID.randomUUID();
        this.transactionNo = uuid.toString().substring(0, 11);

        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        this.date = formatter.format(d);

        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm aa");
        this.time = formatter2.format(d);
    }
}
