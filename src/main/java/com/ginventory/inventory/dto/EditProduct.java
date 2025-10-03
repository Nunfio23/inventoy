package com.ginventory.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProduct {
    private Long id; // Agregamos el ID para identificar el producto a editar
    private String name;
    private int stock;
    private Double price;
    private String category;
}
