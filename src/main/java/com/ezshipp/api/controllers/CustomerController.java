package com.ezshipp.api.controllers;

import com.ezshipp.api.document.Customer;
import com.ezshipp.api.repositories.CustomerRepository;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by srinivasseri on 2/4/18.
 */
@RestController
@Api(value = "/api/customers", description = "a rest service")
@RequestMapping(path = "/api/customers")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class CustomerController implements ControllerConstants {
    @Inject
    CustomerRepository customerRepository;

    @RequestMapping(method=RequestMethod.GET, value="/customers")
    public Iterable<Customer> customer() {
        return customerRepository.findAll();
    }

    @RequestMapping(method= RequestMethod.POST, value="/customers")
    public String save(@RequestBody Customer customer) {
        customerRepository.save(customer);
        //order.getOrderSeqId;
        //return order.getId();
        return null;
    }

    @RequestMapping(method=RequestMethod.GET, value="/customers/{id}")
    public Customer show(@PathVariable String id) {
        return customerRepository.findById(id).get();
    }

    @RequestMapping(method=RequestMethod.PUT, value="/customers/{id}")
    public Customer update(@PathVariable String id, @RequestBody Customer customer) {
        Customer c = customerRepository.findById(id).get();
//        if(customer.getProdName() != null)
//            c.setProdName(customer.getProdName());
//        if(customer.getProdDesc() != null)
//            c.setProdDesc(customer.getProdDesc());
//        if(customer.getProdPrice() != null)
//            c.setProdPrice(customer.getProdPrice());
//        if(customer.getProdImage() != null)
//            c.setProdImage(customer.getProdImage());
//        customerRepository.save(c);
        return c;
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/customers/{id}")
    public String delete(@PathVariable String id) {
        Customer customer = customerRepository.findById(id).get();
        customerRepository.delete(customer);

        return "customer deleted";
    }
}
