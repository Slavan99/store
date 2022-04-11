package com.example.store.controller;

import com.example.store.controller.request.CartItemRequest;
import com.example.store.entity.Cart;
import com.example.store.entity.Product;
import com.example.store.entity.User;
import com.example.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Cart> getCartContent(Authentication auth) {
        User user = (User) auth.getPrincipal();

        return cartService.getCartContentByUser(user.getName());
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Cart> addItemToCart(@RequestBody CartItemRequest cartItemRequest, Authentication auth)
    throws Exception {
        cartItemRequest.setUser((User) auth.getPrincipal());
        cartService.addProductToCart(cartItemRequest);
        return getCartContent(auth);
    }

    @DeleteMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Cart> removeItemFromCart(@RequestBody Product product, Authentication auth) throws Exception {
        User principal = (User) auth.getPrincipal();
        cartService.deleteByUserNameAndProduct(principal.getName(), product);
        return getCartContent(auth);
    }

    @PatchMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Cart> modifyCartItem(@RequestBody CartItemRequest cartItemRequest, Authentication auth)
    throws Exception {
        User principal = (User) auth.getPrincipal();
        cartService.modifyCartItem(principal.getName(), cartItemRequest);
        return getCartContent(auth);
    }


}
