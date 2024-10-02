package com.dlshopping.shoppingcart.model;

import com.dlshopping.shoppingcart.form.CustomerForm;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CustomerInfo implements Serializable{
    private String name;
    private String address;
    private String email;
    private String phone;

    private boolean valid;

    public CustomerInfo() {}

    public CustomerInfo(CustomerForm customerForm) {
        this.name = customerForm.getName();
        this.address = customerForm.getAddress();
        this.email = customerForm.getEmail();
        this.phone = customerForm.getPhone();
        this.valid = customerForm.isValid();
    }

    public boolean validate() {
        return this.name != null && !this.name.isEmpty() &&
                this.email != null && this.email.contains("@") &&
                this.phone != null && !this.phone.isEmpty();
    }

    public boolean isValid() {
        return validate();
    }


}
