package com.example.store.controller;

import com.example.store.entity.Order;
import com.example.store.entity.User;
import com.example.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Order> getAllOrders(Authentication auth) {
        User principal = (User) auth.getPrincipal();
        return orderService.getAllOrders(principal.getName());
    }

    @GetMapping(path = "/last",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Order getLastOrder(Authentication auth) {
        User principal = (User) auth.getPrincipal();
        return orderService.getLastOrder(principal.getName());
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Order checkoutOrder(Authentication auth) throws Exception {
        User principal = (User) auth.getPrincipal();
        orderService.checkoutOrder(principal);
        return getLastOrder(auth);
    }

    @DeleteMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Order cancelOrder(Authentication auth) throws Exception{
        User principal = (User) auth.getPrincipal();
        orderService.cancelOrder(principal.getName());
        return getLastOrder(auth);
    }

}
