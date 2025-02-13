package com.amcom.ambev.order.service;

import com.amcom.ambev.order.model.BuyOrder;
import com.amcom.ambev.order.model.Item;
import com.amcom.ambev.order.model.dto.BuyOrderDTO;
import com.amcom.ambev.order.model.dto.ItemDTO;
import com.amcom.ambev.order.model.enumeration.BuyOrderStatus;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

public interface BuyOrderService {

    BuyOrder saveWithItems(BigInteger orderNumber, Set<ItemDTO> items, String customerDocument);

    BuyOrder updateWithItems(BigInteger orderNumber, Set<ItemDTO> items, BuyOrderStatus status);

    Optional<BuyOrder> findByNumber(BigInteger number);

    void delete(BuyOrder order);

    Set<BuyOrder> findBuyOrders(BuyOrderDTO order);

    BigDecimal calculatePriceOrder(BigInteger orderNumber);

    BigDecimal calculatePriceOrder(Set<Item> items);

}
