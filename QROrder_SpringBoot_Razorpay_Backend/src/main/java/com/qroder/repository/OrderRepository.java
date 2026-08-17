package com.qroder.repository;
import com.qroder.entity.OrderEntity; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface OrderRepository extends JpaRepository<OrderEntity,Long>{
 Optional<OrderEntity> findByToken(String token); Optional<OrderEntity> findByRazorpayOrderId(String id);
 List<OrderEntity> findAllByOrderByCreatedAtDesc();
}