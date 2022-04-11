package com.example.store.service;

import com.example.store.entity.Cart;
import com.example.store.entity.Order;
import com.example.store.entity.OrderStatus;
import com.example.store.entity.User;
import com.example.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order getLastOrder(String username) {
        return orderRepository.findFirstByUserNameOrderByOrderDateTimeDesc(username);
    }

    public List<Order> getAllOrders(String username) {
        return orderRepository.findAllByUserName(username);
    }

    public void checkoutOrder(User user) throws Exception {
        List<Cart> carts = cartService.getCartContentByUser(user.getName());
        if (carts.isEmpty()) {
            throw new Exception("Order is empty!");
        }

        Order order = new Order();
        carts.forEach(order::addCart);
        order.setUser(user);
        order.setTotal(carts.stream().mapToDouble(Cart::getSubTotal).sum());
        order.setOrderStatus(OrderStatus.FINISHED);
        order.setOrderDateTime(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);

        carts.forEach(cart -> {
                          cart.setActive(false);
                          cartService.saveCart(cart);
                      });
    }

    public void cancelOrder(String username) throws Exception {
        Order lastOrder = getLastOrder(username);
        lastOrder.setOrderStatus(OrderStatus.CANCELLED);
        lastOrder.getCart().clear();
        orderRepository.save(lastOrder);
        cartService.deleteByUserName(username);
    }

}
