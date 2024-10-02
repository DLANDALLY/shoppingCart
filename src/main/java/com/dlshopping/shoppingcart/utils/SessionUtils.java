package com.dlshopping.shoppingcart.utils;

import com.dlshopping.shoppingcart.entity.Account;
import com.dlshopping.shoppingcart.entity.Product;
import com.dlshopping.shoppingcart.form.ProductForm;
import com.dlshopping.shoppingcart.model.CartInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.Date;

public class SessionUtils {
    public static CartInfo saveCartInSession(HttpSession session) {
        CartInfo cartInfo = (CartInfo) session.getAttribute("myCart");
        if (cartInfo == null) session.setAttribute("myCart", new CartInfo());
        return cartInfo;
    }
    public static CartInfo getCartInfoSession(HttpSession session) {
        return (CartInfo) session.getAttribute("myCart");
    }
    public static void storeCartInSession(HttpSession session) {
        session.setAttribute("myCart", new CartInfo());
    }

    public static void removeCartInSession(HttpSession session) {
        session.removeAttribute("myCart");
    }

    public static void storeLastOrderedCartInSession(HttpSession session, CartInfo cartInfo) {
        session.setAttribute("lastOrderedCart", cartInfo);
    }

    public static CartInfo getLastOrderedCartInSession(HttpSession session) {
        return (CartInfo) session.getAttribute("lastOrderedCart");
    }

    public static void storeAmountTotal(HttpSession session, double amount){
        session.setAttribute("amountFromSession", amount);
    }

    public static void storeQuantity(HttpSession session, int quantity) {
        session.setAttribute("quantityFromSession", quantity);
    }

    public static void accountFromSession(HttpSession session, Account account) {
        session.setAttribute("accountFromSession", account);
    }

    public static Account accountFromSession(HttpSession session) {
        return (Account) session.getAttribute("account");
    }



    public static Product productMapping(ProductForm productForm) throws IOException {
        Product product = new Product();
        product.setCode(productForm.getCode());
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setImage(productForm.getFileData().getBytes());
        product.setCreateDate(new Date());
        return product;
    }



}
