package com.dlshopping.shoppingcart.utils;

import com.dlshopping.shoppingcart.entity.Product;
import com.dlshopping.shoppingcart.form.ProductForm;
import com.dlshopping.shoppingcart.model.CartInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Date;

// cette classe n'est plus utiliser en attend de suppression
public class Utils {
    // Products in the cart, stored in Session.
    public static CartInfo getCartInSession(HttpServletRequest request) {
        CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");
        if (cartInfo == null)
            request.getSession().setAttribute("myCart", new CartInfo());
        return cartInfo;
    }

    public static void removeCartInSession(HttpServletRequest request) {
        request.getSession().removeAttribute("myCart");
    }

    public static void storeLastOrderedCartInSession(HttpServletRequest request, CartInfo cartInfo) {
        request.getSession().setAttribute("lastOrderedCart", cartInfo);
    }

    public static CartInfo getLastOrderedCartInSession(HttpServletRequest request) {
        return (CartInfo) request.getSession().getAttribute("lastOrderedCart");
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
