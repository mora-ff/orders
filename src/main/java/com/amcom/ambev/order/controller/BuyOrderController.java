package com.amcom.ambev.order.controller;

import com.amcom.ambev.order.model.BuyOrder;
import com.amcom.ambev.order.model.dto.CustomerDTO;
import com.amcom.ambev.order.model.dto.BuyOrderDTO;
import com.amcom.ambev.order.model.dto.ItemDTO;
import com.amcom.ambev.order.model.enumeration.BuyOrderStatus;
import com.amcom.ambev.order.service.BuyOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@RestController
@Tag(name="Orders API" , description = "Orders and products manager")
@RequestMapping("/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
public class BuyOrderController {

    private final BuyOrderService orderService;

    @GetMapping("/{number}")
    public ResponseEntity<Object> getOneOrder(@PathVariable(value = "number") BigInteger orderNumber) {
        Optional<BuyOrder> orderSearch = orderService.findByNumber(orderNumber);
        orderSearch.ifPresent(buyOrder -> buyOrder.setTotalPrice(orderService.calculatePriceOrder(orderNumber)));
        return orderSearch.<ResponseEntity<Object>>map(order -> ResponseEntity.status(HttpStatus.OK).body(order)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found."));
    }

    @GetMapping
    public ResponseEntity<Set<BuyOrder>> getOrders(
            @RequestParam String customerDocument,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerEmail,
            @RequestParam(required = false) BuyOrderStatus orderStatus) {

        log.info("state=init-find-orders , status={} , customer-name={}, customer-email={} , customer-document={}", orderStatus, customerName, customerEmail, customerDocument);

        CustomerDTO customer = CustomerDTO.builder().document(customerDocument).name(customerName).email(customerEmail).build();
        BuyOrderDTO order = BuyOrderDTO.builder().customer(customer).status(orderStatus).build();

        try {
            Set<BuyOrder> orders = orderService.findBuyOrders(order);

            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashSet<>());
            }
            log.info("state=end-success-find-orders , status={}, customer-name={}, customer-email={} , customer-document={}", orderStatus, customerName, customerEmail, customerDocument);

            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("state=error-find-orders, status={}, customer-name={}, customer-email={} , customer-document={}", orderStatus, customerName, customerEmail, customerDocument , e);
            return ResponseEntity.internalServerError().body(new HashSet<>());
        }

    }

    @PostMapping
    public ResponseEntity<Object> saveOrder(@RequestParam BigInteger orderNumber,
                                            @RequestParam String customerDocument,
                                            @Valid @RequestBody Set<ItemDTO> orderItems) {

        log.info("state=init-create-order , order-number={}, customer-document={}", orderNumber, customerDocument);
        try {
            Optional<BuyOrder> order = orderService.findByNumber(orderNumber);
            if (order.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Order already registered.");
            }

            var newOrder = orderService.saveWithItems(orderNumber , orderItems, customerDocument);

            log.info("state=end-success-create-order, order-number={}, customer-document={}", orderNumber, customerDocument);

            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } catch (Exception e) {
            log.error("state=error-create-order, order-number={}, customer-document={}", orderNumber, customerDocument, e);
            return ResponseEntity.internalServerError().body("Internal server error occurred.");
        }
    }

    @PutMapping("/{number}")
    public ResponseEntity<Object> updateOrder(@PathVariable(value = "number") BigInteger orderNumber,
                                              @RequestParam BuyOrderStatus orderStatus,
                                              @RequestBody Set<ItemDTO> orderItems) {

        log.info("state=init-update-order , order-number={}", orderNumber);
        try {
            Optional<BuyOrder> order = orderService.findByNumber(orderNumber);
            if(order.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
            }

            var updatedOrder = orderService.updateWithItems(order.get().getNumber(), orderItems, orderStatus);

            log.info("state=end-success-update-order , order-number={}", orderNumber);

            return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
        } catch (Exception e) {
            log.error("state=error-update-order, order-number={}", orderNumber , e);
            return ResponseEntity.internalServerError().body("Internal server error occurred.");
        }
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Object> deleteOrder(@PathVariable(value = "number") BigInteger orderNumber) {

        log.info("state=init-delete-order , number={}", orderNumber);
        try {
            Optional<BuyOrder> order = orderService.findByNumber(orderNumber);
            if(order.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
            }

            orderService.delete(order.get());
            log.info("state=end-success-delete-order, order-number={}", orderNumber);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("state=error-delete-order, order-number={}", orderNumber, e);
            return ResponseEntity.internalServerError().body("Internal server error occurred.");
        }
    }

}
