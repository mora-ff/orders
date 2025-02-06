package com.amcom.ambev.order.service.impl;

import com.amcom.ambev.order.model.Product;
import com.amcom.ambev.order.model.dto.ProductDTO;
import com.amcom.ambev.order.repository.ProductRepository;
import com.amcom.ambev.order.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public Optional<Product> findByCode(String code) {
        return productRepository.findByCode(code);
    }

    @Override
    public List<Product> findProducts(ProductDTO product) {
        return productRepository.findByFilters(product.code(), product.description());
    }

}
