package com.dlshopping.shoppingcart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
@Table(name = "Accounts")
public class Account {

    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";

    @Id
    @Column(name = "User_Name", length = 20, nullable = false)
    private String userName;

    @Column(name = "Encryted_Password", length = 128, nullable = false)
    private String encrytedPassword;

    @Column(name = "Active", length = 1, nullable = false)
    private boolean active;

    @Column(name = "User_Role", length = 20, nullable = false)
    private String userRole;

    public Account() {}

    public Account(String userName, String encrytedPassword, boolean active, String userRole) {
        this.userName = userName;
        this.encrytedPassword = encrytedPassword;
        this.active = active;
        this.userRole = userRole;
    }
}
