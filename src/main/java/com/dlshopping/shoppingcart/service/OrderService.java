package com.dlshopping.shoppingcart.service;

import com.dlshopping.shoppingcart.entity.Order;
import com.dlshopping.shoppingcart.entity.OrderDetail;
import com.dlshopping.shoppingcart.entity.Product;
import com.dlshopping.shoppingcart.model.*;
import com.dlshopping.shoppingcart.repositories.OrderDetailRepository;
import com.dlshopping.shoppingcart.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ProductService productService;



    /**
     * Get max order number
     * @return the max order number
     */
    public int getMaxOrderNum() {
        //return orderRepository.findByOrderNum();
        Random random = new Random();
        return random.nextInt(100);
    }

    /**
     * Get orders with pagination
     * @param pageable
     * @return the page of orders
     */
    public Page getOrdersWithPage(Pageable pageable){
        Page<Order> orderPages = orderRepository.findAll(pageable);
        return orderPages;
    }


    /**
     * Create a new order
     * @param cartInfo
     * @return the new order
     */
    public Order createOrder(CartInfo cartInfo) {
        int orderNum = this.getMaxOrderNum() + 1;
        System.out.println("## creatOrder ##");
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setCustomerName(customerInfo.getName());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerPhone(customerInfo.getPhone());
        order.setCustomerAddress(customerInfo.getAddress());

        // Persist the order using JPA repository
        orderRepository.save(order);
        return order;
    }

    /**
     * Save an order with order details
     * @param cartInfo
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(CartInfo cartInfo) {
        Order order = createOrder(cartInfo);
        List<OrderDetail> orderDetails = new ArrayList<>();
        Map<String, Product> productCache = new HashMap<>();


        for (CartLineInfo line : cartInfo.getCartLines()) {
            OrderDetail detail = new OrderDetail();

            detail.setId(UUID.randomUUID().toString());
            detail.setOrder(order);
            detail.setAmount(line.getAmount());
            detail.setPrice(line.getProductInfo().getPrice());
            detail.setQuanity(line.getQuantity());

            String code = line.getProductInfo().getCode();
            Product product = productService.getProductByCode(code);
            detail.setProduct(product);

            orderDetails.add(detail);
        }

        orderDetailRepository.saveAll(orderDetails);
        int orderNum = this.getMaxOrderNum() + 1;
        cartInfo.setOrderNum(orderNum);
    }


    /**
     * Get Order by ID
     * @param orderId
     * @return Order
     * @throws EntityNotFoundException
     */
    public Order findOrder(String orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
    }

    /**
     * Get OrderInfo by ID
     * @param orderId
     * @return OrderInfo
     * @throws EntityNotFoundException
     */
    public OrderInfo getOrderInfo(String orderId){
        Order order = findOrder(orderId);
        if (order == null) {
            throw new EntityNotFoundException("Order not found with ID: " + orderId);
        }
        return new OrderInfo(order);
    }

    /**
     * List OrderDetailInfos by Order ID
     * @param orderId
     * @return List<OrderDetailInfo>
     */
    public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
        return orderDetailRepository.findOrderDetailsByOrderId(orderId);
    }
}
