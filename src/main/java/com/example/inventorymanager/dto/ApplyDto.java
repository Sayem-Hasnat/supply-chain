package com.example.inventorymanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ApplyDto implements Serializable {
    private long applyId;
    private String username;
    private String password;

    private String applyImagePath;
}
