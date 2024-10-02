package com.dlshopping.shoppingcart.repositories;

import com.dlshopping.shoppingcart.entity.OrderDetail;
import com.dlshopping.shoppingcart.model.OrderDetailInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Query(value = "SELECT * FROM OrderDetail d WHERE d.order.id = :orderId", nativeQuery = true)
    List<OrderDetailInfo> findOrderDetailsByOrderId(@Param("orderId") String orderId);

}
