package com.example.store.repository;

import com.example.store.entity.Cart;
import com.example.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findFirstByUserNameOrderByOrderDateTimeDesc(String username);

    List<Order> findAllByUserName(String username);
}
