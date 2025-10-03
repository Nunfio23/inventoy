package com.ginventory.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowStockAlertDTO {
    private Long id;
    private String name;
    private int currentStock;
    private String category;
    private String alertMessage;
}
