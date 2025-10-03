package com.ginventory.inventory.mapper;

import com.ginventory.inventory.dto.CreateProductDTO;
import com.ginventory.inventory.dto.EditProduct; // Asegúrate de importar EditProduct también
import com.ginventory.inventory.dto.ShowProductDTO;
import com.ginventory.inventory.model.Product;

public class ProductMapper {
    public static ShowProductDTO toDto(Product product){
        return new ShowProductDTO(
            product.getId(),
            product.getName(),
            product.getStock(),
            product.getPrice(),
            product.getCategory()
        );
    }

    public static Product toEntity(CreateProductDTO createProductDTO){
        Product product = new Product();
        product.setName(createProductDTO.getName());
        product.setStock(createProductDTO.getStock());
        product.setPrice(createProductDTO.getPrice());
        product.setCategory(createProductDTO.getCategory());
        return product;
    }
    
    public static Product toEntity(EditProduct editProduct) {
        Product product = new Product();
        product.setId(editProduct.getId());
        product.setName(editProduct.getName());
        product.setStock(editProduct.getStock());
        product.setPrice(editProduct.getPrice());
        product.setCategory(editProduct.getCategory());
        return product;
    }
}
