package com.example.store.controller.request;

import com.example.store.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemRequest {

    private Integer id;

    private Integer quantity;

    private User user;


}
