package com.example.inventorymanager.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
public class CategoryDto {
    private long categoryId;
    private String categoryName;

    List<ItemDto> itemDtoList;
}
