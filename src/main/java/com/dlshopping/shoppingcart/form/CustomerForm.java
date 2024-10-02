package com.dlshopping.shoppingcart.form;

import com.dlshopping.shoppingcart.model.CustomerInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerForm {
    private String name;
    private String address;
    private String email;
    private String phone;
    private boolean valid;

    public CustomerForm() {}

    public CustomerForm(CustomerInfo customerInfo) {
        if (customerInfo != null) {
            this.name = customerInfo.getName();
            this.address = customerInfo.getAddress();
            this.email = customerInfo.getEmail();
            this.phone = customerInfo.getPhone();
        }
    }


}
