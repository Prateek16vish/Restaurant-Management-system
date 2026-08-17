package com.qroder.dto;
public record PaymentVerifyRequest(String token,String razorpayPaymentId,String razorpayOrderId,String razorpaySignature){}