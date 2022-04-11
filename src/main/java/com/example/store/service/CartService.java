package com.example.store.service;

import com.example.store.controller.request.CartItemRequest;
import com.example.store.entity.Cart;
import com.example.store.entity.Product;
import com.example.store.entity.User;
import com.example.store.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public List<Cart> getCartContentByUser(String username) {
        return cartRepository.findAllByUserName(username).stream().filter(Cart::getActive).collect(
                Collectors.toList());
    }

    public Cart addProductToCart(CartItemRequest cartItemRequest) throws Exception {
        Integer requestProductId = cartItemRequest.getId();
        Integer quantity = cartItemRequest.getQuantity();
        User user = cartItemRequest.getUser();

        Optional<Cart> byUserNameAndProductId =
                cartRepository.findByUserNameAndProductId(user.getName(), requestProductId);
        if (byUserNameAndProductId.isPresent()) {
            throw new Exception("Record already exists!");
        }

        Product productById = productService.getProductById(requestProductId);
        if (productById.getAvailable() < quantity) {
            throw new Exception("Not enough amount of product!");
        }

        Cart cart = new Cart();

        productById.setAvailable(productById.getAvailable() - quantity);
        productService.saveProduct(productById);

        cart.setProduct(productById);
        cart.setSubTotal(productById.getPrice() * quantity);
        cart.setQuantity(quantity);
        cart.setUser(user);
        cart.setActive(true);
        return saveCart(cart);
    }

    public void deleteByUserName(String username) throws Exception {
        returnProductsFromCartToStore(username);
        cartRepository.deleteByUserName(username);
    }

    public void deleteActiveCartsByUserName(String username) throws Exception {
        returnProductsFromCartToStore(username);
        cartRepository.deleteByUserNameAndActive(username, true);
    }

    public void deleteByUserNameAndProduct(String username, Product product) throws Exception {
        returnProductFromCartToStore(username, product.getId());
        cartRepository.deleteByUserNameAndProductId(username, product.getId());
    }

    public Cart modifyCartItem(String username, CartItemRequest cartItemRequest) throws Exception {
        Integer requestProductId = cartItemRequest.getId();
        Integer quantity = cartItemRequest.getQuantity();

        Cart cartByUserNameAndProductId = cartRepository.findByUserNameAndProductId(username, requestProductId)
                                                        .orElseThrow();
        Integer oldQuantity = cartByUserNameAndProductId.getQuantity();

        Product productById = productService.getProductById(requestProductId);
        if (productById.getAvailable() < quantity) {
            throw new Exception("Not enough amount of product!");
        }

        productById.setAvailable(productById.getAvailable() - quantity + oldQuantity);
        productService.saveProduct(productById);

        cartByUserNameAndProductId.setQuantity(quantity);
        cartByUserNameAndProductId.setSubTotal(productById.getPrice() * quantity);
        return cartRepository.save(cartByUserNameAndProductId);

    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    private void returnProductsFromCartToStore(String username) {
        productService
                .getAllProducts()
                .forEach(product -> returnProductFromCartToStore(username, product.getId()));
    }

    private void returnProductFromCartToStore(String username, Integer productId) {
        List<Cart> cartList = cartRepository
                .findAllByUserName(username);
        for (Cart cart : cartList) {
            Product product = cart.getProduct();
            if (Objects.equals(product.getId(), productId)) {
                product.setAvailable(product.getAvailable() + cart.getQuantity());
                productService.saveProduct(product);
            }
        }
    }

}
