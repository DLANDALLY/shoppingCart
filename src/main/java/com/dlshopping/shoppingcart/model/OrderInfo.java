package com.dlshopping.shoppingcart.model;

import com.dlshopping.shoppingcart.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class OrderInfo implements Serializable {
    private String id;
    private Date orderDate;
    private int orderNum;
    private double amount;

    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private String customerPhone;
    private List<OrderDetailInfo> details;

    public OrderInfo() {
    }

    public OrderInfo(String id, Date orderDate, int orderNum, //
                     double amount, String customerName, String customerAddress, //
                     String customerEmail, String customerPhone) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderNum = orderNum;
        this.amount = amount;

        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
    }

    public OrderInfo(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderNum = order.getOrderNum();
        this.amount = order.getAmount();

        this.customerName = order.getCustomerName();
        this.customerAddress = order.getCustomerAddress();
        this.customerEmail = order.getCustomerEmail();
        this.customerPhone = order.getCustomerPhone();
    }


    // Renvoyer une copie de la liste pour éviter des modifications non voulues
    public List<OrderDetailInfo> getDetails() {
        return details != null ? new ArrayList<>(details) : null;
    }
}
