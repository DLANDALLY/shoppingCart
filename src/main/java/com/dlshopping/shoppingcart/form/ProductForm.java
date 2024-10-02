package com.dlshopping.shoppingcart.form;

import com.dlshopping.shoppingcart.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ProductForm {
    private String code;
    private String name;
    private double price;
    private boolean newProduct = false;
    private MultipartFile fileData;

    public ProductForm() {
        this.newProduct= true;
    }

    public ProductForm(Product product) {
        this.code = product.getCode();
        this.name = product.getName();
        this.price = product.getPrice();
    }

    public boolean isNewProduct() {
        return newProduct;
    }

    public void setNewProduct(boolean newProduct) {
        this.newProduct = newProduct;
    }
}
