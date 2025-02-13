package com.amcom.ambev.order.controller;

import com.amcom.ambev.order.model.BuyOrder;
import com.amcom.ambev.order.service.BuyOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
public class BuyOrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BuyOrderService orderService;

    @InjectMocks
    private BuyOrderController buyOrderController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(buyOrderController).build();
    }

    @Test
    public void testGetOneOrderSuccess() throws Exception {
        BuyOrder order = new BuyOrder();
        order.setNumber(BigInteger.valueOf(1));
        order.setTotalPrice(BigDecimal.valueOf(100));

        when(orderService.findByNumber(BigInteger.valueOf(1))).thenReturn(Optional.of(order));
        when(orderService.calculatePriceOrder(BigInteger.valueOf(1))).thenReturn(BigDecimal.valueOf(100));

        mockMvc.perform(get("/orders/{number}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.totalPrice").value(100));

        verify(orderService, times(1)).findByNumber(BigInteger.valueOf(1));
        verify(orderService, times(1)).calculatePriceOrder(BigInteger.valueOf(1));
    }

    @Test
    public void testGetOneOrderNotFound() throws Exception {
        when(orderService.findByNumber(BigInteger.valueOf(1))).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/{number}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found."));

        verify(orderService, times(1)).findByNumber(BigInteger.valueOf(1));
    }

    @Test
    public void testGetOrdersSuccess() throws Exception {
        BuyOrder order = new BuyOrder();
        order.setNumber(BigInteger.valueOf(1));

        when(orderService.findBuyOrders(any())).thenReturn(Set.of(order));

        mockMvc.perform(get("/orders")
                        .param("customerDocument", "123456789")
                        .param("customerName", "John Doe")
                        .param("customerEmail", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(1));

        verify(orderService, times(1)).findBuyOrders(any());
    }

    @Test
    public void testGetOrdersNotFound() throws Exception {
        when(orderService.findBuyOrders(any())).thenReturn(Set.of());

        mockMvc.perform(get("/orders")
                        .param("customerDocument", "123456789")
                        .param("customerName", "John Doe")
                        .param("customerEmail", "john.doe@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("[]"));

        verify(orderService, times(1)).findBuyOrders(any());
    }

    @Test
    public void testSaveOrderConflict() throws Exception {
        BuyOrder order = new BuyOrder();
        order.setNumber(BigInteger.valueOf(1));

        when(orderService.findByNumber(BigInteger.valueOf(1))).thenReturn(Optional.of(order));

        mockMvc.perform(post("/orders")
                        .param("orderNumber", "1")
                        .param("customerDocument", "123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Order already registered."));

        verify(orderService, times(1)).findByNumber(BigInteger.valueOf(1));
    }

    @Test
    public void testDeleteOrderSuccess() throws Exception {
        BuyOrder order = new BuyOrder();
        order.setNumber(BigInteger.valueOf(1));

        when(orderService.findByNumber(BigInteger.valueOf(1))).thenReturn(Optional.of(order));

        mockMvc.perform(delete("/orders/{number}", 1))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).findByNumber(BigInteger.valueOf(1));
        verify(orderService, times(1)).delete(order);
    }

    @Test
    public void testDeleteOrderNotFound() throws Exception {
        when(orderService.findByNumber(BigInteger.valueOf(1))).thenReturn(Optional.empty());

        mockMvc.perform(delete("/orders/{number}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found."));

        verify(orderService, times(1)).findByNumber(BigInteger.valueOf(1));
    }

}
