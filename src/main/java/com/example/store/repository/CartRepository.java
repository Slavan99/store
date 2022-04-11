package com.example.store.repository;

import com.example.store.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUserName(String username);
    @Transactional
    void deleteByUserName(String username) throws Exception;

    @Transactional
    void deleteByUserNameAndActive(String username, boolean active) throws Exception;

    @Transactional
    void deleteByUserNameAndProductId(String username, Integer productId) throws Exception;

    Optional<Cart> findByUserNameAndProductId(String username, Integer productId);
}
