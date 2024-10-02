package com.dlshopping.shoppingcart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Products")
public class Product {
    @Id
    @Column(name = "Code", length = 20, nullable = false)
    private String code;

    @Column(name = "Name", length = 255, nullable = false)
    private String name;

    @Column(name = "Price", nullable = false)
    private double price;

    @Lob
    @Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Date", nullable = false)
    private Date createDate;
}
