package com.dlshopping.shoppingcart.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class CartLineInfo implements Serializable {
    private ProductInfo productInfo;
    private int quantity;

    public CartLineInfo() {this.quantity = 0;}

    public CartLineInfo(ProductInfo productInfo, int quantity) {
        this.productInfo = productInfo;
        this.quantity = quantity;
    }

    public double getAmount() {
        return this.productInfo.getPrice() * this.quantity;
    }


    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

