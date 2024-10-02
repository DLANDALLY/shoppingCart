package com.dlshopping.shoppingcart.model;

import com.dlshopping.shoppingcart.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductInfo implements Serializable {
    private String code;
    private String name;
    private double price;

    public ProductInfo() {
    }


    public ProductInfo(Product product) {
        this.code = product.getCode();
        this.name = product.getName();
        this.price = product.getPrice();
    }

}
