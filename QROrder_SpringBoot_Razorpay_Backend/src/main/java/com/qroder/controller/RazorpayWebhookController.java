package com.qroder.controller;
import com.qroder.entity.OrderEntity; import com.qroder.repository.OrderRepository; import com.qroder.service.RazorpayService; import org.json.*; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/razorpay")
public class RazorpayWebhookController{
 final RazorpayService razor; final OrderRepository repo;
 public RazorpayWebhookController(RazorpayService r,OrderRepository p){razor=r;repo=p;}
 @PostMapping("/webhook") public ResponseEntity<?> webhook(@RequestBody String body,@RequestHeader(value="X-Razorpay-Signature",required=false)String sig){
  try{
   if(sig==null||!razor.verifyWebhook(body,sig))return ResponseEntity.badRequest().body(Map.of("error","Invalid signature"));
   JSONObject root=new JSONObject(body);String event=root.optString("event");
   if("payment.captured".equals(event)){
    JSONObject e=root.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");String rp=e.optString("order_id"),pid=e.optString("id");
    repo.findByRazorpayOrderId(rp).ifPresent(o->{o.setRazorpayPaymentId(pid);o.setPaymentStatus("PAID");if(!"Completed".equals(o.getStatus()))o.setStatus("Order Received");repo.save(o);});
   }
   return ResponseEntity.ok(Map.of("received",true));
  }catch(Exception e){return ResponseEntity.badRequest().body(Map.of("error","Webhook failed"));}
 }
}