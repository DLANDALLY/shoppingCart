package com.dlshopping.shoppingcart.validator;

import com.dlshopping.shoppingcart.entity.Product;
import com.dlshopping.shoppingcart.form.ProductForm;
import com.dlshopping.shoppingcart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProductFormValidator implements Validator {
    @Autowired
    private ProductService productService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ProductForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductForm productForm = (ProductForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "NotEmpty.productForm.code");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.productForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.productForm.price");

        String code = productForm.getCode();
        if (code != null && !code.trim().isEmpty()) {
            validateCode(errors, productForm, code);
        }
    }
    private void validateCode(Errors errors, ProductForm productForm, String code) {
        if (code.matches("\\s+")) errors.rejectValue("code", "Pattern.productForm.code");
        else if (productForm.isNewProduct() && productService.getProductByCode(code) != null)
            errors.rejectValue("code", "Duplicate.productForm.code");
    }
}
