package com.amcom.ambev.order.service.impl;

import com.amcom.ambev.order.model.Customer;
import com.amcom.ambev.order.model.dto.CustomerDTO;
import com.amcom.ambev.order.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.amcom.ambev.order.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Override
    public Optional<Customer> findByDocument(String document) {
        return customerRepository.findByDocument(document);
    }

    @Override
    public List<Customer> findCustomers(CustomerDTO customer) {
        return customerRepository.findByFilters(customer.name(), customer.document());
    }
}
