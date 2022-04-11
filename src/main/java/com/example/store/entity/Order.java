package com.example.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JsonIgnoreProperties({"email", "password", "active", "roles","enabled","username","authorities",""})
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Cart> cart = new ArrayList<>();

    private Timestamp orderDateTime;

    private Double total;

    private OrderStatus orderStatus;

    public List<Cart> getCart() {
        return cart;
    }

    public void addCart(Cart cart) {
        this.cart.add(cart);
    }
}
