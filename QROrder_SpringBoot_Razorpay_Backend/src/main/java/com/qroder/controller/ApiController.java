package com.qroder.controller;
import com.qroder.dto.*; import com.qroder.entity.*; import com.qroder.repository.*; import com.qroder.service.OrderService; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.time.LocalDateTime; import java.util.*;
@RestController @RequestMapping("/api")
public class ApiController{
 final OrderService service; final FeedbackRepository feedback;
 public ApiController(OrderService s,FeedbackRepository f){service=s;feedback=f;}
 @PostMapping("/orders") public ResponseEntity<?> create(@RequestBody CreateOrderRequest r){try{return ResponseEntity.ok(service.create(r).toMap());}catch(Exception e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}}
 @PostMapping("/payments/verify") public ResponseEntity<?> verify(@RequestBody PaymentVerifyRequest r){try{OrderEntity o=service.verify(new OrderService.PaymentVerifyRequestHolder(r.token(),r.razorpayPaymentId(),r.razorpayOrderId(),r.razorpaySignature()));return ResponseEntity.ok(Map.of("success",true,"token",o.getToken(),"status",o.getStatus(),"paymentStatus",o.getPaymentStatus()));}catch(SecurityException e){return ResponseEntity.badRequest().body(Map.of("success",false,"error","Payment verification failed"));}catch(Exception e){return ResponseEntity.badRequest().body(Map.of("success",false,"error",e.getMessage()));}}
 @GetMapping("/orders") public List<OrderEntity> orders(){return service.all();}
 @GetMapping("/orders/{token}") public OrderEntity order(@PathVariable String token){return service.byToken(token);}
 @PatchMapping("/orders/{token}/status") public ResponseEntity<?> status(@PathVariable String token,@RequestBody Map<String,String> b){try{return ResponseEntity.ok(service.status(token,b.get("status")));}catch(Exception e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}}
 @PostMapping("/feedback") public ResponseEntity<?> feedback(@RequestBody FeedbackRequest r){if(r.rating()==null||r.rating()<1||r.rating()>5)return ResponseEntity.badRequest().body(Map.of("error","Rating must be 1-5"));FeedbackEntity f=new FeedbackEntity();f.setToken(r.token());f.setRating(r.rating());f.setFeedback(r.feedback());f.setCreatedAt(LocalDateTime.now());feedback.save(f);return ResponseEntity.ok(Map.of("success",true));}
 @GetMapping("/feedback") public List<FeedbackEntity> feedback(){return feedback.findAllByOrderByCreatedAtDesc();}
}