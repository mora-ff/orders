package com.amcom.ambev.order.repository;

import com.amcom.ambev.order.model.BuyOrder;
import com.amcom.ambev.order.model.enumeration.BuyOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BuyOrderRepository extends JpaRepository<BuyOrder, UUID> {

    @Query("SELECT o FROM BuyOrder o JOIN o.customer c WHERE "
            + "(:status IS NULL OR o.status = :status) AND "
            + "(:name IS NULL OR c.name ILIKE %:name%) AND "
            + "(:email IS NULL OR c.email ILIKE %:email%) AND "
            + "(:document IS NULL OR c.document = :document)")
    Set<BuyOrder> findByFilters(@Param("status") BuyOrderStatus status,
                                @Param("name") String name,
                                @Param("email") String email,
                                @Param("document") String document);

    Optional<BuyOrder> findByNumber(BigInteger number);

}
