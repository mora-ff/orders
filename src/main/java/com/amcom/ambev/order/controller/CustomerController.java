package com.amcom.ambev.order.controller;

import com.amcom.ambev.order.model.Customer;
import com.amcom.ambev.order.model.dto.CustomerDTO;
import com.amcom.ambev.order.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Customer API" , description = "Customers manager")
@RequestMapping("/customers")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{document}")
    public ResponseEntity<Object> getOneCustomer(@PathVariable(value = "document") String document) {
        Optional<Customer> customerSearch = customerService.findByDocument(document);
        return customerSearch.<ResponseEntity<Object>>map(customer -> ResponseEntity.status(HttpStatus.OK).body(customer)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found."));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(
            @RequestParam(required = false) String document,
            @RequestParam(required = false) String name) {

        log.info("state=init-find-customers, customer-document={}, customer-name={}", document, name);

        try {
            List<Customer> customers = customerService.findCustomers(CustomerDTO.builder().name(name).document(document).build());

            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
            log.info("state=end-success-find-customers, customer-document={}, customer-name={}", document, name);

            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            log.error("state=error-find-customers, customer-document={}, customer-name={}", document, name, e);
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }

    }

}
