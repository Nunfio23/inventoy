package com.ginventory.inventory.service;

import java.util.List;

import com.ginventory.inventory.dto.CreateProductDTO;
import com.ginventory.inventory.dto.EditProduct;
import com.ginventory.inventory.dto.LowStockAlertDTO;
import com.ginventory.inventory.dto.ShowProductDTO;

public interface IProductService {

    List<ShowProductDTO> getAllProducts();
    List<ShowProductDTO> getProductsByIds(List<Long> ids);
    List<ShowProductDTO> getByCategory(String category);
    List<ShowProductDTO> getByName(String name);
    ShowProductDTO getById(Long id);
    CreateProductDTO createProduct(CreateProductDTO createProductDTO);
    EditProduct editProduct(EditProduct editProduct);
    ShowProductDTO decreaseStock(Long id, int quantity); // Nuevo método para bajar stock
    ShowProductDTO increaseStock(Long id, int quantity); // Nuevo método para subir stock
    List<LowStockAlertDTO> getLowStockAlerts(); // Método para obtener alertas de stock bajo

}
