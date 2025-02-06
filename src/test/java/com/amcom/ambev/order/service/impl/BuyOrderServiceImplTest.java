package com.amcom.ambev.order.service.impl;

import com.amcom.ambev.order.model.*;
import com.amcom.ambev.order.model.dto.ItemDTO;
import com.amcom.ambev.order.model.enumeration.BuyOrderStatus;
import com.amcom.ambev.order.repository.BuyOrderRepository;
import com.amcom.ambev.order.service.CustomerService;
import com.amcom.ambev.order.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuyOrderServiceImplTest {

    @Mock
    private BuyOrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private BuyOrderServiceImpl buyOrderService;

    private Customer customer;
    private ItemDTO itemDTO;
    private Product product;
    private BuyOrder buyOrder;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setDocument("123456789");

        product = new Product();
        product.setCode("P123");
        product.setPrice(BigDecimal.valueOf(100));

        itemDTO = new ItemDTO("P123", 2, BigDecimal.ZERO);

        buyOrder = BuyOrder.builder()
                .number(BigInteger.ONE)
                .status(BuyOrderStatus.ORDER_SENT)
                .customer(customer)
                .createDate(LocalDateTime.now(ZoneId.of("UTC")))
                .updateDate(LocalDateTime.now(ZoneId.of("UTC")))
                .build();
    }

    @Test
    void saveWithItemsShouldSaveOrderWhenValidData() {
        when(customerService.findByDocument("123456789")).thenReturn(Optional.of(customer));
        when(productService.findByCode("P123")).thenReturn(Optional.of(product));
        when(orderRepository.save(any(BuyOrder.class))).thenReturn(buyOrder);

        BuyOrder result = buyOrderService.saveWithItems(BigInteger.ONE, List.of(itemDTO), "123456789");

        assertNotNull(result);
        assertEquals(BigInteger.ONE, result.getNumber());
        assertEquals(BuyOrderStatus.ORDER_SENT, result.getStatus());
        verify(orderRepository).save(any(BuyOrder.class));
    }

    @Test
    void updateWithItemsShouldUpdateOrderWhenValidData() {
        when(orderRepository.findByNumber(BigInteger.ONE)).thenReturn(Optional.of(buyOrder));
        when(productService.findByCode("P123")).thenReturn(Optional.of(product));
        when(orderRepository.save(any(BuyOrder.class))).thenReturn(buyOrder);

        BuyOrder result = buyOrderService.updateWithItems(BigInteger.ONE, List.of(itemDTO), BuyOrderStatus.ORDER_SENT);

        assertNotNull(result);
        assertEquals(BuyOrderStatus.ORDER_SENT, result.getStatus());
        verify(orderRepository).save(any(BuyOrder.class));
    }

    @Test
    void findByNumberShouldReturnOrderWhenExists() {
        when(orderRepository.findByNumber(BigInteger.ONE)).thenReturn(Optional.of(buyOrder));

        Optional<BuyOrder> result = buyOrderService.findByNumber(BigInteger.ONE);

        assertTrue(result.isPresent());
        assertEquals(BigInteger.ONE, result.get().getNumber());
    }

    @Test
    void deleteShouldCallRepositoryDelete() {
        doNothing().when(orderRepository).delete(buyOrder);

        buyOrderService.delete(buyOrder);

        verify(orderRepository).delete(buyOrder);
    }

    @Test
    void calculatePriceOrderShouldReturnCorrectTotalPrice() {
        Item item = Item.builder()
                .amount(2)
                .product(product)
                .unitPrice(BigDecimal.valueOf(100))
                .fullDiscount(BigDecimal.valueOf(10))
                .build();

        BigDecimal totalPrice = buyOrderService.calculatePriceOrder(List.of(item));

        assertEquals(BigDecimal.valueOf(190), totalPrice);
    }
}