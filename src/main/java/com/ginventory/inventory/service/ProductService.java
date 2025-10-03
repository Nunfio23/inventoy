package com.ginventory.inventory.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ginventory.inventory.dto.CreateProductDTO;
import com.ginventory.inventory.dto.EditProduct;
import com.ginventory.inventory.dto.LowStockAlertDTO;
import com.ginventory.inventory.dto.ShowProductDTO;
import com.ginventory.inventory.mapper.ProductMapper;
import com.ginventory.inventory.repository.ProductRepository;
import com.ginventory.inventory.model.Product;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    
    // Límites de stock configurables
    private static final int MAX_STOCK_LIMIT = 300; // Límite máximo de stock
    private static final int MAX_DECREASE_LIMIT = 100; // Límite máximo para disminuir stock
    private static final int LOW_STOCK_THRESHOLD = 5; // Umbral de stock bajo

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    

    @Override
    public List<ShowProductDTO> getAllProducts() {  
        return productRepository.findAll().stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ShowProductDTO> getProductsByIds(List<Long> ids) {
        List<Product> products = productRepository.findAllById(ids);
        return products.stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }
    

    @Override
    public List<ShowProductDTO> getByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ShowProductDTO> getByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ShowProductDTO getById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return ProductMapper.toDto(product);
    }

    @Override
    public CreateProductDTO createProduct(CreateProductDTO createProductDTO) {
        // Validar límite máximo de stock
        if (createProductDTO.getStock() > MAX_STOCK_LIMIT) {
            throw new IllegalArgumentException("El stock no puede exceder " + MAX_STOCK_LIMIT + " unidades. Stock solicitado: " + createProductDTO.getStock());
        }
        
        Product product = new Product();
        product.setName(createProductDTO.getName());
        product.setStock(createProductDTO.getStock());
        product.setPrice(createProductDTO.getPrice());
        product.setCategory(createProductDTO.getCategory());

        Product savedProduct = productRepository.save(product);
        return new CreateProductDTO(
            savedProduct.getName(),
            savedProduct.getStock(),
            savedProduct.getPrice(),
            savedProduct.getCategory()
        );
    }

    @Override
    public EditProduct editProduct(EditProduct editProduct) {
        // Validar límite máximo de stock
        if (editProduct.getStock() > MAX_STOCK_LIMIT) {
            throw new IllegalArgumentException("El stock no puede exceder " + MAX_STOCK_LIMIT + " unidades. Stock solicitado: " + editProduct.getStock());
        }
        
        Product product = productRepository.findById(editProduct.getId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + editProduct.getId()));

        product.setName(editProduct.getName());
        product.setStock(editProduct.getStock());
        product.setPrice(editProduct.getPrice());
        product.setCategory(editProduct.getCategory());

        Product updatedProduct = productRepository.save(product);

        return new EditProduct(
            updatedProduct.getId(),
            updatedProduct.getName(),
            updatedProduct.getStock(),
            updatedProduct.getPrice(),
            updatedProduct.getCategory()
        );
    }


    @Override
    public ShowProductDTO decreaseStock(Long id, int quantity) {
        // Validar límite máximo de disminución
        if (quantity > MAX_DECREASE_LIMIT) {
            throw new IllegalArgumentException("No se pueden eliminar más de " + MAX_DECREASE_LIMIT + " unidades a la vez. Cantidad solicitada: " + quantity);
        }
        
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Stock insuficiente para el producto con id: " + id + ". Stock disponible: " + product.getStock() + ", cantidad solicitada: " + quantity);
        }

        product.setStock(product.getStock() - quantity);
        Product updatedProduct = productRepository.save(product);
        
        // Verificar si el stock quedó bajo después de la operación
        checkLowStockAlert(updatedProduct);
        
        return ProductMapper.toDto(updatedProduct);
    }


    @Override
    public ShowProductDTO increaseStock(Long id, int quantity) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        // Validar que no se exceda el límite máximo de stock
        int newStock = product.getStock() + quantity;
        if (newStock > MAX_STOCK_LIMIT) {
            throw new IllegalArgumentException("No se puede exceder el límite máximo de " + MAX_STOCK_LIMIT + " unidades. Stock actual: " + product.getStock() + ", cantidad a agregar: " + quantity + ", total resultante: " + newStock);
        }

        product.setStock(newStock);
        Product updatedProduct = productRepository.save(product);
        return ProductMapper.toDto(updatedProduct);
    }

    @Override
    public List<LowStockAlertDTO> getLowStockAlerts() {
        return productRepository.findAll().stream()
            .filter(product -> product.getStock() <= LOW_STOCK_THRESHOLD)
            .map(product -> {
                String alertMessage = product.getStock() == 0 
                    ? "¡STOCK AGOTADO! Reabastecer urgentemente" 
                    : "Stock bajo: " + product.getStock() + " unidades restantes";
                
                return new LowStockAlertDTO(
                    product.getId(),
                    product.getName(),
                    product.getStock(),
                    product.getCategory(),
                    alertMessage
                );
            })
            .collect(Collectors.toList());
    }

    /**
     * Método auxiliar para verificar si un producto tiene stock bajo
     * y generar una alerta si es necesario
     */
    private void checkLowStockAlert(Product product) {
        if (product.getStock() <= LOW_STOCK_THRESHOLD) {
            String alertMessage = product.getStock() == 0 
                ? "¡STOCK AGOTADO! Reabastecer urgentemente" 
                : "Stock bajo: " + product.getStock() + " unidades restantes";
            
            // Aquí podrías agregar lógica para enviar notificaciones
            // como emails, logs, o notificaciones push
            System.out.println("⚠️ ALERTA DE STOCK BAJO:");
            System.out.println("Producto: " + product.getName() + " (ID: " + product.getId() + ")");
            System.out.println("Categoría: " + product.getCategory());
            System.out.println("Stock actual: " + product.getStock());
            System.out.println("Mensaje: " + alertMessage);
            System.out.println("----------------------------------------");
        }
    }

}
