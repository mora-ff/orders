package com.amcom.ambev.order.service;

import com.amcom.ambev.order.model.Customer;
import com.amcom.ambev.order.model.dto.CustomerDTO;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Optional<Customer> findByDocument(String document);

    List<Customer> findCustomers(CustomerDTO customer);

}
