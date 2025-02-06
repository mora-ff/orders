package com.amcom.ambev.order.service;

import com.amcom.ambev.order.model.BuyOrder;
import com.amcom.ambev.order.model.Item;
import com.amcom.ambev.order.model.dto.BuyOrderDTO;
import com.amcom.ambev.order.model.dto.ItemDTO;
import com.amcom.ambev.order.model.enumeration.BuyOrderStatus;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface BuyOrderService {

    BuyOrder saveWithItems(BigInteger orderNumber, List<ItemDTO> items, String customerDocument);

    BuyOrder updateWithItems(BigInteger orderNumber, List<ItemDTO> items, BuyOrderStatus status);

    Optional<BuyOrder> findByNumber(BigInteger number);

    void delete(BuyOrder order);

    List<BuyOrder> findBuyOrders(BuyOrderDTO order);

    BigDecimal calculatePriceOrder(BigInteger orderNumber);

    BigDecimal calculatePriceOrder(List<Item> items);

}
