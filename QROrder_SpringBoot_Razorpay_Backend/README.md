# QROrder backend

Java 21 + Spring Boot 3.5.5 + MySQL + Razorpay Java SDK 1.4.9.

## Start
1. Install JDK 21, Maven and MySQL.
2. Edit `src/main/resources/application.properties` and set your MySQL password.
3. Create Razorpay Test API keys.
4. Set:
   RAZORPAY_KEY_ID
   RAZORPAY_KEY_SECRET
   RAZORPAY_WEBHOOK_SECRET
5. Run: `mvn spring-boot:run`
6. Backend: http://localhost:8080

## API
POST /api/orders
POST /api/payments/verify
GET /api/orders
GET /api/orders/{token}
PATCH /api/orders/{token}/status
POST /api/feedback
GET /api/feedback
POST /api/razorpay/webhook

The customer must create the Razorpay order through POST /api/orders before Checkout opens.
The browser sends payment_id/order_id/signature back to POST /api/payments/verify.
The server verifies the signature with the secret and only then changes the order to PAID.

Never put RAZORPAY_KEY_SECRET in frontend code.
