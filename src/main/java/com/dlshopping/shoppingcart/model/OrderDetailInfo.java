package com.dlshopping.shoppingcart.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class OrderDetailInfo implements Serializable {
    private String id;

    private String productCode;
    private String productName;

    private int quantity;
    private double price;
    private double amount;

    public OrderDetailInfo() {
    }

    public OrderDetailInfo(String id, String productCode, String productName, int quantity, double price) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public double getAmount() {
        return this.price * this.quantity;
    }

}
