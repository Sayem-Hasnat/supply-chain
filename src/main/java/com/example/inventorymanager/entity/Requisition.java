package com.example.inventorymanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "requisition")
@Getter
@Setter
public class Requisition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requisitionId;
    @Column
    private String requisitionNo;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "requisition_product",
            joinColumns = @JoinColumn(name = "requisition_id"),
            inverseJoinColumns = @JoinColumn(name = "wishlist_id"))
    private List<Wishlist> productList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "requisition_transaction",
            joinColumns = @JoinColumn(name = "requisition_id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    private List<Transaction> transactionList;

    @Column
    private double payable;
    @Column
    private double paid;
    @Column
    private String paymentStatus;

    @Column
    private String status="Sent To Inventory Manager";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Requisition() {
        UUID uuid = UUID.randomUUID();
        this.requisitionNo =uuid.toString().substring(0,11);
    }
}
