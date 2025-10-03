package com.ginventory.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowProductDTO {
    private Long id;
    private String name;
    private int stock;
    private Double price;
    private String category;
}
