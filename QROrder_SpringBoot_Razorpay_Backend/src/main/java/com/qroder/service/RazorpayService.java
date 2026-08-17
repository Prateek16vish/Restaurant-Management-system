package com.qroder.service;
import com.qroder.entity.OrderEntity; import com.qroder.repository.OrderRepository; import com.razorpay.*; import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
@Service
public class RazorpayService{
 final OrderRepository repo; final String keyId,keySecret,webhookSecret;
 public RazorpayService(OrderRepository r,@Value("${razorpay.key-id}")String k,@Value("${razorpay.key-secret}")String s,@Value("${razorpay.webhook-secret}")String w){repo=r;keyId=k;keySecret=s;webhookSecret=w;}
 public JSONObject create(OrderEntity o)throws Exception{
  RazorpayClient c=new RazorpayClient(keyId,keySecret); JSONObject x=new JSONObject();
  x.put("amount",o.getTotalPaise()); x.put("currency","INR"); x.put("receipt",o.getToken()); x.put("payment_capture",1);
  com.razorpay.Order ro=c.orders.create(x); o.setRazorpayOrderId(ro.get("id")); o.setPaymentStatus("CREATED"); repo.save(o); return ro.toJson();
 }
 public boolean verify(OrderEntity o,String paymentId,String sig)throws Exception{
  JSONObject x=new JSONObject(); x.put("razorpay_order_id",o.getRazorpayOrderId()); x.put("razorpay_payment_id",paymentId); x.put("razorpay_signature",sig);
  return Utils.verifyPaymentSignature(x,keySecret);
 }
 public boolean verifyWebhook(String body,String sig)throws Exception{return Utils.verifyWebhookSignature(body,sig,webhookSecret);}
 public String keyId(){return keyId;}
}