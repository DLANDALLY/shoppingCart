package com.dlshopping.shoppingcart.controller;


import com.dlshopping.shoppingcart.entity.Product;
import com.dlshopping.shoppingcart.form.CustomerForm;
import com.dlshopping.shoppingcart.model.CartInfo;
import com.dlshopping.shoppingcart.model.CartLineInfo;
import com.dlshopping.shoppingcart.model.CustomerInfo;
import com.dlshopping.shoppingcart.service.AccountService;
import com.dlshopping.shoppingcart.service.OrderService;
import com.dlshopping.shoppingcart.service.ProductService;
import com.dlshopping.shoppingcart.utils.SessionUtils;
import com.dlshopping.shoppingcart.validator.CustomerFormValidator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;



import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerFormValidator customerFormValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) return;
        System.out.println("Target=" + target);

        if (target instanceof CartInfo) {}
        else if (target instanceof CustomerForm)
            dataBinder.setValidator(customerFormValidator);

        // Case update quantity in cart
        //(@ModelAttribute("cartForm") @Validated CartInfo cartForm)
        if (target.getClass() == CartInfo.class) {}

        // Case save customer information.
        //(@ModelAttribute @Validated CustomerInfo customerForm)
        else if (target.getClass() == CustomerForm.class) {
            dataBinder.setValidator(customerFormValidator);
        }
    }

    @ModelAttribute("myCart")
    public CartInfo getCartInSession() {
        return new CartInfo();
    }



    @GetMapping("/error")
    public String handleError() {
        System.out.println("######### /main/error  handleError##########");
        return "errorpage";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/productList")
    public String listProduct(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getProductsWithPage(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "productList";
    }

    @GetMapping("/buyProduct")
    public String listProductHandler(HttpSession session,
                                     @RequestParam(value ="code", defaultValue ="") String code) {
        System.out.println("######### /main/BuyProcduct ##########");
        productService.buyProduct(session, code, 1);

        return "redirect:/shoppingCart";
    }

    @GetMapping("/shoppingCartRemoveProduct" )
    public String removeProductHandler(HttpSession session, Model model,
                                       @RequestParam(value = "code", defaultValue = "") String code) {
        productService.removeProduct(session, code);
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart")
    public String shoppingCartUpdateQty(HttpSession session,
                                        @ModelAttribute("cartForm") CartInfo cartForm) {
        System.out.println("######### /main/shoppingCart UPDATE POST##########");

        for (CartLineInfo line : cartForm.getCartLines()) {
            //SessionUtils.storeQuantity(session, line.getQuantity());

            SessionUtils.storeQuantity(session, line.getQuantity());
            SessionUtils.getCartInfoSession(session);
            CartInfo cartInfo = SessionUtils.getCartInfoSession(session);
            cartInfo.updateProduct(session,line.getProductInfo().getCode(), line.getQuantity());
        }

        return "redirect:/shoppingCart";
    }


    @GetMapping("/shoppingCart")
    public String shoppingCartHandler(HttpSession session, Model model) {
        System.out.println("######### /main/shoppingCartHandler GET ##########");
        CartInfo cartInfo = SessionUtils.getCartInfoSession(session);

        model.addAttribute("cartForm", cartInfo);
        return "shoppingCart";
    }

    @GetMapping("/shoppingCartCustomer")
    public String shoppingCartCustomerForm(HttpSession session, Model model) {
        System.out.println("######### /main/ShoppingCartCustomer GET 10 ##########");

        CartInfo cartInfo = SessionUtils.getCartInfoSession(session);
        //SessionUtils.storeQuantity(session, cartInfo.getQuantityTotal());

        if (cartInfo.isEmpty()) return "redirect:/shoppingCart";

        CustomerForm customerForm = new CustomerForm(cartInfo.getCustomerInfo());
        model.addAttribute("customerForm", customerForm);

        return "shoppingCartCustomer";
    }

    @PostMapping("/shoppingCartCustomer")
    public String shoppingCartCustomerSave(HttpSession session, Model model,
            @ModelAttribute("customerForm") @Validated CustomerForm customerForm,
            BindingResult result) {
        System.out.println("######### /main/shopCartCust POST 11 ##########");
        if (result.hasErrors()) {
            customerForm.setValid(false);
            model.addAttribute("errorMessage", "Erreur Formulaire");
            return "shoppingCartCustomer";
        }

        customerForm.setValid(true);
        SessionUtils.storeCartInSession(session);
        CartInfo cartInfo = SessionUtils.getCartInfoSession(session);

        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);

        return "redirect:/shoppingCartConfirmation";
    }

    @GetMapping("/shoppingCartConfirmation")
    public String shoppingCartConfirmationReview(HttpSession session, Model model) {
        System.out.println("######### /main/shopCartConf GET 12 ##########");
        CartInfo cartInfo = SessionUtils.getCartInfoSession(session);

        if (cartInfo == null) return "redirect:/shoppingCart";
        else if (!cartInfo.isValidCustomer()) return "redirect:/shoppingCartCustomer";

        orderService.saveOrder(cartInfo);//Test L223

        SessionUtils.storeLastOrderedCartInSession( session, cartInfo);


        model.addAttribute("myCart", cartInfo);

        return "shoppingCartConfirmation";
    }

    @PostMapping("/shoppingCartConfirmation")
    public String shoppingCartConfirmationSave(HttpSession session, Model model) {
        System.out.println("######### /main/shopCartConf POST 13 ##########");

        /*CartInfo cartInfo = SessionUtils.getCartInfoSession(session);
        System.out.println("#####  1  ###########");
        if (cartInfo.isEmpty()) {
            System.out.println("#####  2  ###########");
            return "redirect:/shoppingCart";}
        else if (!cartInfo.isValidCustomer()) {
            System.out.println("#####  3  ###########");
            return "redirect:/shoppingCartCustomer";}

        try {
            System.out.println("#####  4  ###########");
            orderService.saveOrder(cartInfo);
            System.out.println("#####  5  ###########");
        } catch (Exception e) {
            System.out.println("#####  6  ###########");
            return "shoppingCartConfirmation";}*/
        System.out.println("#####  7  ###########");

        //SessionUtils.storeLastOrderedCartInSession( session, cartInfo);
        SessionUtils.removeCartInSession(session);


        return "redirect:/shoppingCartFinalize";
    }

    @GetMapping("/shoppingCartFinalize")
    public String shoppingCartFinalize(HttpSession session, Model model) {
        System.out.println("######### /main/shopCartFinal GET ##########");
        CartInfo lastOrderedCart = (CartInfo) SessionUtils.getLastOrderedCartInSession(session);
        if (lastOrderedCart == null) return "redirect:/shoppingCart";

        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "shoppingCartFinalize";
    }

    @GetMapping("/productImage")
    public void productImage(
            @RequestParam("code") String code, HttpServletResponse response) throws IOException {
        Product product = productService.getProductByCode(code);

        if (product == null || product.getImage() == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
            return;
        }

        // Déterminer le type de contenu basé sur les données d'image si possible
        response.setContentType("image/jpeg,  image/jpg, image/png, image/gif"); // Exemple pour JPEG, adapte selon le type d'image
        response.getOutputStream().write(product.getImage());
        response.getOutputStream().flush(); // Assure que tout est écrit avant de fermer
    }
}
