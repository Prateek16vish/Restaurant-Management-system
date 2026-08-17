package com.qroder.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
@Entity @Table(name="restaurant_orders")
public class OrderEntity{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 @Column(nullable=false,unique=true) String token;
 String tableNumber,orderType,status,paymentStatus,razorpayOrderId,razorpayPaymentId;
 Long totalPaise;
 @Column(length=500) String instructions;
 LocalDateTime createdAt;
 @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER) List<OrderItemEntity> items=new ArrayList<>();
 public Long getId(){return id;} public String getToken(){return token;} public void setToken(String v){token=v;}
 public String getTableNumber(){return tableNumber;} public void setTableNumber(String v){tableNumber=v;}
 public String getOrderType(){return orderType;} public void setOrderType(String v){orderType=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getPaymentStatus(){return paymentStatus;} public void setPaymentStatus(String v){paymentStatus=v;}
 public String getRazorpayOrderId(){return razorpayOrderId;} public void setRazorpayOrderId(String v){razorpayOrderId=v;}
 public String getRazorpayPaymentId(){return razorpayPaymentId;} public void setRazorpayPaymentId(String v){razorpayPaymentId=v;}
 public Long getTotalPaise(){return totalPaise;} public void setTotalPaise(Long v){totalPaise=v;}
 public String getInstructions(){return instructions;} public void setInstructions(String v){instructions=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
 public List<OrderItemEntity> getItems(){return items;}
}