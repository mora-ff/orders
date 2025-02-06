package com.amcom.ambev.order.service.impl;

import com.amcom.ambev.order.model.BuyOrder;
import com.amcom.ambev.order.model.Customer;
import com.amcom.ambev.order.model.Item;
import com.amcom.ambev.order.model.dto.BuyOrderDTO;
import com.amcom.ambev.order.model.dto.ItemDTO;
import com.amcom.ambev.order.model.enumeration.BuyOrderStatus;
import com.amcom.ambev.order.repository.BuyOrderRepository;
import com.amcom.ambev.order.service.BuyOrderService;
import com.amcom.ambev.order.service.CustomerService;
import com.amcom.ambev.order.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BuyOrderServiceImpl implements BuyOrderService {

    private final BuyOrderRepository orderRepository;

    private final ProductService productService;

    private final CustomerService customerService;

    @Transactional
    @Override
    public BuyOrder saveWithItems(BigInteger orderNumber, List<ItemDTO> items, String customerDocument) {

        Customer customer = customerService.findByDocument(customerDocument).orElseThrow(IllegalArgumentException::new);

        var newOrder = BuyOrder.builder()
                .number(orderNumber)
                .status(BuyOrderStatus.ORDER_SENT)
                .customer(customer)
                .createDate(LocalDateTime.now(ZoneId.of("UTC")))
                .updateDate(LocalDateTime.now(ZoneId.of("UTC")))
                .build();

        for (ItemDTO item : items) {
            var product = productService.findByCode(item.productCode()).orElseThrow(IllegalArgumentException::new);
            var newItem = Item.builder()
                    .amount(item.amount())
                    .product(product)
                    .unitPrice(product.getPrice())
                    .fullDiscount(item.fullDiscount())
                    .createDate(LocalDateTime.now(ZoneId.of("UTC")))
                    .updateDate(LocalDateTime.now(ZoneId.of("UTC")))
                    .build();
            newOrder.addItem(newItem);
        }
        return orderRepository.save(newOrder);
    }

    @Transactional
    @Override
    public BuyOrder updateWithItems(BigInteger orderNumber, List<ItemDTO> items, BuyOrderStatus status) {

        BuyOrder order = this.findByNumber(orderNumber).orElseThrow(IllegalArgumentException::new);
        order.setStatus(status);
        order.setUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        order.getItens().clear();

        for (ItemDTO item : items) {
            var product = productService.findByCode(item.productCode()).orElseThrow(IllegalArgumentException::new);
            var newItem = Item.builder()
                    .amount(item.amount())
                    .order(order)
                    .product(product)
                    .unitPrice(product.getPrice())
                    .fullDiscount(item.fullDiscount())
                    .createDate(LocalDateTime.now(ZoneId.of("UTC")))
                    .updateDate(LocalDateTime.now(ZoneId.of("UTC")))
                    .build();
            order.addItem(newItem);
        }

        return orderRepository.save(order);
    }

    @Override
    public Optional<BuyOrder> findByNumber(BigInteger number) {
        return orderRepository.findByNumber(number);
    }

    @Override
    public void delete(BuyOrder order) {
        orderRepository.delete(order);
    }

    @Override
    public List<BuyOrder> findBuyOrders(BuyOrderDTO order) {
       List<BuyOrder> orders = orderRepository.findByFilters(order.status(),
                order.customer().name(),
                order.customer().email(),
                order.customer().document());

        orders.forEach(o -> o.setTotalPrice(this.calculatePriceOrder(o.getItens())));

        return orderRepository.findByFilters(order.status(),
                                             order.customer().name(),
                                             order.customer().email(),
                                             order.customer().document());
    }

    @Override
    public BigDecimal calculatePriceOrder(BigInteger orderNumber) {
        var order = orderRepository.findByNumber(orderNumber).orElseThrow(IllegalArgumentException::new);
        return calculatePriceOrder(order.getItens());
    }

    @Override
    public BigDecimal calculatePriceOrder(List<Item> items) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(Item item : items) {
            totalPrice = totalPrice.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getAmount())).subtract(item.getFullDiscount()));
        }
        return totalPrice;
    }

}
