package com.amcom.ambev.order.repository;

import com.amcom.ambev.order.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

}
