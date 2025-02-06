package com.amcom.ambev.order.repository;

import com.amcom.ambev.order.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByCode(String code);

    @Query("SELECT p FROM Product p  WHERE "
            + "(:code IS NULL OR p.code ILIKE %:code%) AND "
            + "(:description IS NULL OR p.description ILIKE %:description%)")
    List<Product> findByFilters(@Param("code") String code,
                                 @Param("description") String description);

}
