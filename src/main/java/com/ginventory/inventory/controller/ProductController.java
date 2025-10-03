package com.ginventory.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ginventory.inventory.dto.CreateProductDTO;
import com.ginventory.inventory.dto.EditProduct;
import com.ginventory.inventory.dto.LowStockAlertDTO;
import com.ginventory.inventory.dto.ShowProductDTO;
import com.ginventory.inventory.service.IProductService;

@RestController
@RequestMapping("/cs")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShowProductDTO>> getAllProducts() {
        List<ShowProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Endpoint para obtener un producto por ID individual
    @GetMapping("/by-id/{id}")
    public ResponseEntity<ShowProductDTO> getProductById(@PathVariable Long id) {
        ShowProductDTO product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<ShowProductDTO>> getProductsByIds(@RequestParam List<Long> ids) {
        List<ShowProductDTO> products = productService.getProductsByIds(ids);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ShowProductDTO>> getProductsByCategory(@PathVariable String category) {
        List<ShowProductDTO> products = productService.getByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<ShowProductDTO>> getProductsByName(@RequestParam String name) {
        List<ShowProductDTO> products = productService.getByName(name);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateProductDTO> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        CreateProductDTO createdProduct = productService.createProduct(createProductDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<EditProduct> editProduct(@PathVariable Long id, @RequestBody EditProduct editProduct) {
        // Asegurarse de que el ID del path coincida con el ID en el cuerpo si está presente
        if (editProduct.getId() == null || !editProduct.getId().equals(id)) {
            editProduct.setId(id);
        }
        EditProduct updatedProduct = productService.editProduct(editProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    @PutMapping("/decrease-stock/{id}")
    public ResponseEntity<ShowProductDTO> decreaseStock(@PathVariable Long id, @RequestParam int quantity) {
        ShowProductDTO updatedProduct = productService.decreaseStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }

    @PutMapping("/increase-stock/{id}")
    public ResponseEntity<ShowProductDTO> increaseStock(@PathVariable Long id, @RequestParam int quantity) {
        ShowProductDTO updatedProduct = productService.increaseStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/low-stock-alerts")
    public ResponseEntity<List<LowStockAlertDTO>> getLowStockAlerts() {
        List<LowStockAlertDTO> alerts = productService.getLowStockAlerts();
        return ResponseEntity.ok(alerts);
    }
}
