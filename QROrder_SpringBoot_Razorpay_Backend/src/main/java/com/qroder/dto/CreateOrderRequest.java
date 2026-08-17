package com.qroder.dto;
import java.util.List;
public record CreateOrderRequest(String tableNumber,String orderType,String instructions,List<Item> items){
 public record Item(Long id,String name,String category,Long pricePaise,Integer quantity,String icon){}
}