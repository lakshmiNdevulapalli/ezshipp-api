package com.ezshipp.api.controllers;

import com.ezshipp.api.document.Order;
import com.ezshipp.api.exception.BusinessException;
import com.ezshipp.api.exception.BusinessExceptionCode;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.model.ClientOrder;
import com.ezshipp.api.repositories.OrderRepository;
import com.ezshipp.api.responses.OrderResponse;
import com.ezshipp.api.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by srinivasseri on 2/4/18.
 */
@RestController
@Api(value = "/api/v1/orders", description = "a rest service")
@RequestMapping(path = "/api/v1/orders")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)

public class OrderController implements ControllerConstants {

    @Inject
    OrderRepository orderRepository;

    @Inject
    OrderService orderService;

    @RequestMapping(method=RequestMethod.GET)
    //public List<Order> getAllOrders() throws ServiceException {
    public OrderResponse getAllOrders() throws BusinessException, ServiceException {
        //return orderService.findAllOrders();
        //return orderService.centralPublicationOrders(true);
        orderService.findGeoLocation();
        return new OrderResponse();
    }

    @RequestMapping(method= RequestMethod.POST)
    public String save(@RequestBody Order order) throws BusinessException, ServiceException {
        orderRepository.save(order);
        return order.getId();
    }

    @RequestMapping(method=RequestMethod.GET, value="/{id}")
    public Order show(@PathVariable String id) throws BusinessException, ServiceException {
        return orderRepository.findById(id).get();
    }

    @RequestMapping(method=RequestMethod.GET, value="/find/{orderId}")
    public List<Order> findByOrderId(@PathVariable String orderId) throws BusinessException, ServiceException {
        return orderService.findByOrderId(orderId);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ApiOperation(
            position = 200,
            value = "Find orders using QUERY(customerName and customerNumber)",
            notes = "Find orders using customerNumbers",
            response = List.class)
    @ApiResponses(value = {@ApiResponse(code = SUCCESS_CODE, message = SUCCESS_MESSAGE),
            @ApiResponse(code = BUSINESS_EXCEPTION_CODE, message = BUSINESS_EXCEPTION_MESSAGE),
            @ApiResponse(code = SERVICE_EXCEPTION_CODE, message = SERVICE_EXCEPTION_MESSAGE)})
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public List<Order> findByCustomerDetails(@RequestParam String customerNumber, @RequestParam String customerName)
            throws BusinessException, ServiceException {

        if (customerName == null || customerName.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.NULL_CUSTOMER_NAME);
        }

        if (customerNumber == null || customerNumber.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.NULL_CUSTOMER_NUMBER);
        }

        List<Order> orderList = orderService.findByCustomerNameOrNumber(customerNumber, customerName);
        return orderList;
    }

    @RequestMapping(method=RequestMethod.GET, value="/pendingorders")
    public OrderResponse pendingOrders(@RequestParam String onlyCount) throws BusinessException, ServiceException {
        return orderService.pendingOrders(Boolean.valueOf(onlyCount));
    }

    @RequestMapping(method=RequestMethod.GET, value="/clientorders")
    public List<ClientOrder> clientOrders(@RequestParam String onlyCount) throws BusinessException, ServiceException {
        return orderService.clientOrders(Boolean.valueOf(onlyCount));
    }

    @RequestMapping(method=RequestMethod.PUT, value="/{id}")
    public Order update(@PathVariable String id, @RequestBody Order order) throws BusinessException, ServiceException {
        Order existingOrder = orderRepository.findById(id).get();
        existingOrder.setOrderseqId(order.getOrderseqId());
        existingOrder.setOrder_datetime(order.getOrder_datetime());
        existingOrder.setStatus(order.getStatus());
        existingOrder.setCustomerName(order.getCustomerName());
        existingOrder.setCustomerPhone(order.getCustomerPhone());
        existingOrder.setDeliverycharge(order.getDeliverycharge());
        existingOrder.setTotal_amount(order.getTotal_amount());
        existingOrder.setSubtotal_amount(order.getSubtotal_amount());
        orderRepository.save(existingOrder);
        return order;
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/{id}")
    public String delete(@PathVariable String id) throws BusinessException, ServiceException{
        Order order = orderRepository.findById(id).get();
        orderRepository.delete(order);

        return "order deleted";
    }
}
