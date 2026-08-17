package com.qroder.entity;
import jakarta.persistence.*;
@Entity @Table(name="order_items")
public class OrderItemEntity{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="order_id") OrderEntity order;
 Long foodId,pricePaise; String name,category,icon; Integer quantity;
 public Long getId(){return id;} public OrderEntity getOrder(){return order;} public void setOrder(OrderEntity v){order=v;}
 public Long getFoodId(){return foodId;} public void setFoodId(Long v){foodId=v;} public Long getPricePaise(){return pricePaise;} public void setPricePaise(Long v){pricePaise=v;}
 public String getName(){return name;} public void setName(String v){name=v;} public String getCategory(){return category;} public void setCategory(String v){category=v;}
 public String getIcon(){return icon;} public void setIcon(String v){icon=v;} public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
}