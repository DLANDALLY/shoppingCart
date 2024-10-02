package com.dlshopping.shoppingcart.validator;

import com.dlshopping.shoppingcart.form.CustomerForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CustomerFormValidator implements Validator {
    //private EmailValidator emailValidator = EmailValidator.getInstance();

    // This validator only checks for the CustomerForm.
    @Override
    public boolean supports(Class<?> clazz) {return CustomerForm.class.equals(clazz);}


    @Override
    public void validate(Object target, Errors errors) {
        CustomerForm custInfo = (CustomerForm) target;

        // Check the fields of CustomerForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.customerForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.customerForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.customerForm.address");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.customerForm.phone");

        // Validation pour le champ "email"
        if (custInfo.getEmail() == null) {
            errors.rejectValue("email", "email.invalid", "l'adresse mail est requis");
        }
        if(!validerEmail(custInfo.getEmail()) || custInfo.getEmail() == null){
            errors.rejectValue("email", "email.invalid", "L'adresse mail n'est pas valide");
        }
    }

    public static boolean validerEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

}
