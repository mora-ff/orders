package com.amcom.ambev.order.repository;

import com.amcom.ambev.order.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByDocument(String document);

    @Query("SELECT c FROM Customer c  WHERE "
            + "(:name IS NULL OR c.name ILIKE %:name%) AND "
            + "(:document IS NULL OR c.document = :document)")
    List<Customer> findByFilters(@Param("name") String name,
                                @Param("document") String document);

}
