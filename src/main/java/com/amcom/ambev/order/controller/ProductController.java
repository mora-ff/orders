package com.amcom.ambev.order.controller;

import com.amcom.ambev.order.model.Product;
import com.amcom.ambev.order.model.dto.ProductDTO;
import com.amcom.ambev.order.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Product API" , description = "Products manager")
@RequestMapping("/products")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{code}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "code") String code) {
        Optional<Product> productSearch = productService.findByCode(code);
        return productSearch.<ResponseEntity<Object>>map(product -> ResponseEntity.status(HttpStatus.OK).body(product)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String description) {

        log.info("state=init-find-products, product-code={}, product-description={}", code, description);

        try {
            List<Product> products = productService.findProducts(ProductDTO.builder().code(code).description(description).build());

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
            log.info("state=end-success-find-products, product-code={}, product-description={}", code, description);

            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("state=error-find-products, product-code={}, product-description={}", code, description , e);
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }

    }

}
