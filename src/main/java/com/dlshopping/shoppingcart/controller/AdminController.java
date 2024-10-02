package com.dlshopping.shoppingcart.controller;

import com.dlshopping.shoppingcart.entity.Order;
import com.dlshopping.shoppingcart.entity.Product;
import com.dlshopping.shoppingcart.form.ProductForm;
import com.dlshopping.shoppingcart.model.OrderDetailInfo;
import com.dlshopping.shoppingcart.model.OrderInfo;
import com.dlshopping.shoppingcart.service.AccountService;
import com.dlshopping.shoppingcart.service.OrderService;
import com.dlshopping.shoppingcart.service.ProductService;
import com.dlshopping.shoppingcart.utils.SessionUtils;
import com.dlshopping.shoppingcart.validator.ProductFormValidator;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductFormValidator productFormValidator;

    @Autowired
    private AccountService accountService;


    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null)
            return;
        if (target instanceof ProductForm)
            dataBinder.setValidator(productFormValidator);

        System.out.println("Target=" + target);

    }

    @GetMapping("/admin/login")
    public String login(Principal principal) {
        System.out.println("######### /admin/login GET ##########");
        return "login";
    }

    @PostMapping("/admin/login")
    public String login( RedirectAttributes redirectAttributes, HttpSession session) {
        System.out.println("######### /admin/login  POST ##########");
        redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
        if(accountService.getUsername() != null){
            SessionUtils.accountFromSession(session, accountService.getUsername());}

        return "redirect:/index";
    }

    //A supprimer
    @GetMapping("/admin/index")
    public String home() {
        System.out.println("######### /admin/index ##########");
        return "index";
    }

    @GetMapping( "/admin/accountInfo")
    public String accountInfo(HttpSession session, Principal principal, Model model) {
        System.out.println("######### /admin/accountInfo ########## Principal = "+ principal.getName());
        session.setAttribute("username", principal.getName());
        model.addAttribute("userDetails", principal);

        return "accountInfo";
    }

    @GetMapping("/admin/orderList")
    public String orderList(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderService.getOrdersWithPage(pageable);
        model.addAttribute("paginationResult", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());

        if (orderPage.getTotalPages() > 1) {
            List<Integer> pageNumbers = IntStream.range(0, orderPage.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "orderList";
    }

    @GetMapping( "/admin/product")
    public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        ProductForm productForm = new ProductForm();
        if (!code.isEmpty()) {
            try {
                Product product = productService.getProductByCode(code);
                if (product != null) productForm = new ProductForm(product);
            } catch (Exception e) {
                System.out.println("Error retrieving product with code: "+ e.getMessage());
            }
        } else {
            productForm.setNewProduct(true);
        }

        model.addAttribute("productForm", productForm);
        return "product";
    }


    @PostMapping("/admin/product")
    public String productSave(Model model,
                              @ModelAttribute("productForm") @Validated ProductForm productForm,
                              BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", result.toString());
            return "product";}

        try {
            productService.saveOrUpdateProduct(productForm);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "product";
        }

        return "redirect:/productList";
    }


    @GetMapping("/admin/order")
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        System.out.println("######### /admin/order ##########");
        if (orderId == null || orderId.isEmpty()) return "redirect:/admin/orderList";

        OrderInfo orderInfo = this.orderService.getOrderInfo(orderId);
        if (orderInfo == null) return "redirect:/admin/orderList";

        List<OrderDetailInfo> details = this.orderService.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);
        model.addAttribute("orderInfo", orderInfo);

        return "order";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session){
        System.out.println("######### /admin/logout ##########");
        session.invalidate();
        return "redirect: /index";
    }
}
