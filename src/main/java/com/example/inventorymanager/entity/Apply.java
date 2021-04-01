package com.example.inventorymanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "apply")
@Getter
@Setter
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long applyId;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String nidTinImage;

    @Column
    private String status="pending";
}
