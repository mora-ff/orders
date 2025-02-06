package com.amcom.ambev.order.service;

import com.amcom.ambev.order.model.Product;
import com.amcom.ambev.order.model.dto.ProductDTO;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findByCode(String code);

    List<Product> findProducts(ProductDTO product);

}
