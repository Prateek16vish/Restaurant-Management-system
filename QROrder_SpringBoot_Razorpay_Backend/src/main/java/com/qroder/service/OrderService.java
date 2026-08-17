package com.qroder.service;
import com.qroder.dto.CreateOrderRequest; import com.qroder.entity.*; import com.qroder.repository.OrderRepository; import org.json.JSONObject; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.LocalDateTime; import java.util.*;
@Service
public class OrderService{
 final OrderRepository repo; final RazorpayService razor;
 public OrderService(OrderRepository r,RazorpayService z){repo=r;razor=z;}
 @Transactional public JSONObject create(CreateOrderRequest r)throws Exception{
  if(r.items()==null||r.items().isEmpty())throw new IllegalArgumentException("Cart is empty");
  OrderEntity o=new OrderEntity(); o.setToken("Q"+(100+(int)(Math.random()*900))); o.setTableNumber(r.tableNumber()); o.setOrderType(r.orderType()); o.setInstructions(r.instructions()); o.setStatus("PAYMENT_PENDING"); o.setPaymentStatus("PENDING"); o.setCreatedAt(LocalDateTime.now());
  long total=0;
  for(CreateOrderRequest.Item i:r.items()){
   if(i.quantity()==null||i.quantity()<1||i.pricePaise()==null||i.pricePaise()<0)throw new IllegalArgumentException("Invalid item");
   OrderItemEntity x=new OrderItemEntity(); x.setOrder(o); x.setFoodId(i.id()); x.setName(i.name()); x.setCategory(i.category()); x.setPricePaise(i.pricePaise()); x.setQuantity(i.quantity()); x.setIcon(i.icon()); o.getItems().add(x); total+=i.pricePaise()*i.quantity();
  }
  o.setTotalPaise(total); repo.save(o); JSONObject rp=razor.create(o); JSONObject out=new JSONObject(); out.put("token",o.getToken()); out.put("razorpayKeyId",razor.keyId()); out.put("razorpayOrderId",rp.getString("id")); out.put("amount",total); out.put("currency","INR"); return out;
 }
 public List<OrderEntity> all(){return repo.findAllByOrderByCreatedAtDesc();}
 public OrderEntity byToken(String t){return repo.findByToken(t).orElseThrow(()->new IllegalArgumentException("Order not found"));}
 @Transactional public OrderEntity status(String t,String s){if(!List.of("PAYMENT_PENDING","Order Received","Preparing","Ready","Completed","Cancelled").contains(s))throw new IllegalArgumentException("Invalid status");OrderEntity o=byToken(t);o.setStatus(s);return repo.save(o);}
 @Transactional public OrderEntity verify(PaymentVerifyRequestHolder p)throws Exception{
  OrderEntity o=byToken(p.token()); if(!p.rpOrder().equals(o.getRazorpayOrderId()))throw new SecurityException("Order mismatch");
  if("PAID".equals(o.getPaymentStatus()))return o;
  if(!razor.verify(o,p.paymentId(),p.signature()))throw new SecurityException("Invalid payment signature");
  o.setRazorpayPaymentId(p.paymentId());o.setPaymentStatus("PAID");o.setStatus("Order Received");return repo.save(o);
 }
 public record PaymentVerifyRequestHolder(String token,String paymentId,String rpOrder,String signature){}
}