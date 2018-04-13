package com.ezshipp.api.service;

import com.ezshipp.api.document.Customer;
import com.ezshipp.api.exception.BusinessException;
import com.ezshipp.api.exception.BusinessExceptionCode;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.repositories.CustomerRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class CustomerService {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private MongoTemplate mongoTemplate;

    public Customer getCustomerById(String id) throws BusinessException, ServiceException {
        Optional<Customer> optional = customerRepository.findById(id);

        if (optional == null || !optional.isPresent())   {
            throw new BusinessException(BusinessExceptionCode.CUSTOMER_NOT_FOUND);
        }

        return optional.get();
    }
}
