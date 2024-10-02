package com.dlshopping.shoppingcart.repositories;

import com.dlshopping.shoppingcart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
   // @Query(value = "SELECT o.order_num FROM Orders o", nativeQuery = true)
    @Query(value = "SELECT order_num FROM public.orders;", nativeQuery = true)
    int findByOrderNum();

}
