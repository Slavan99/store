package com.example.store.service;

import com.example.store.controller.request.CartItemRequest;
import com.example.store.entity.Cart;
import com.example.store.entity.Product;
import com.example.store.entity.User;
import com.example.store.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @Autowired
    @InjectMocks
    private CartService cartService;

    private List<Cart> cartList;

    private Product product1, product2;

    private User user1;

    @BeforeEach
    private void prepareCarts() {
        product1 = new Product(1, "Sas", 15, 5.0);
        product2 = new Product(2, "Rar", 10, 10.0);
        user1 = new User("User1@mail", "User1", "password1", true, new HashSet<>());
        Cart cart1 = new Cart(1, user1, product1, 5, 25.0, true);
        Cart cart2 = new Cart(2, user1, product2, 5, 50.0, true);
        cartList = List.of(cart1, cart2);
    }

    @Test
    public void getCartContentByUserTest() {
        String username = "User1";
        Mockito.lenient()
               .when(cartRepository.findAllByUserName(username)
                                   .stream().filter(Cart::getActive)
                                   .collect(Collectors.toList()))
               .thenReturn(cartList);
        assertEquals(
                cartRepository.findAllByUserName(username)
                              .stream().filter(Cart::getActive)
                              .collect(Collectors.toList()),
                cartService.getCartContentByUser(username));
    }

    @Test
    public void addProductToCartTest() throws Exception {
        CartItemRequest cartItemRequest = new CartItemRequest(product1.getId(), 5, user1);
        Integer requestProductId = cartItemRequest.getId();
        Mockito.lenient()
               .when(cartRepository.findByUserNameAndProductId(user1.getName(), requestProductId))
               .thenReturn(Optional.empty());
        Mockito.lenient()
                .when(productService.getProductById(requestProductId))
                .thenReturn(product1);
        Mockito.lenient()
               .when(productService.saveProduct(product1))
               .thenReturn(product1);
        Cart resultCart = new Cart(1, user1, product1, 5, 25.0, true);
        Mockito.lenient()
               .when(cartRepository.save(any()))
               .thenReturn(resultCart);
        Cart actual = cartService.addProductToCart(cartItemRequest);
        assertEquals(resultCart, actual);
    }

    @Test
    public void modifyCartItemTest() throws Exception {
        CartItemRequest cartItemRequest = new CartItemRequest(product1.getId(), 10, user1);
        Integer requestProductId = cartItemRequest.getId();
        Cart cart = cartList.get(0);
        Mockito.lenient()
               .when(cartRepository.findByUserNameAndProductId(user1.getName(), requestProductId))
               .thenReturn(Optional.ofNullable(cart));
        Mockito.lenient()
               .when(productService.getProductById(requestProductId))
               .thenReturn(product1);
        Mockito.lenient()
               .when(productService.saveProduct(product1))
               .thenReturn(product1);
        Cart resultCart = new Cart(1, user1, product1, 10, 25.0, true);
        Mockito.lenient()
               .when(cartRepository.save(any()))
               .thenReturn(resultCart);
        Cart actual = cartService.modifyCartItem(user1.getName(), cartItemRequest);
        assertEquals(resultCart, actual);
    }

    @Test
    public void saveCartTest() {
        Cart cart = new Cart(1, user1, product1, 5, 25.0, true);
        Mockito.lenient()
               .when(cartRepository.save(cart))
               .thenReturn(cart);
        Cart saveCart = cartService.saveCart(cart);
        assertEquals(cart, saveCart);
    }
}