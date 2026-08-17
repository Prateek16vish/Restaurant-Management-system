# Connect your two HTML pages

Backend base URL:
`http://localhost:8080/api`

## Customer page
Add before `</head>`:
```html
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
```

When the customer clicks Place Order:
1. POST cart data to `/api/orders`.
2. Receive `razorpayKeyId`, `razorpayOrderId`, `amount`, `token`.
3. Open Razorpay Checkout with that order ID.
4. On success POST `token`, `razorpayPaymentId`, `razorpayOrderId`, `razorpaySignature` to `/api/payments/verify`.
5. Show confirmation only after verification succeeds.

Example:
```js
const r = await fetch("http://localhost:8080/api/orders", {
  method:"POST",
  headers:{"Content-Type":"application/json"},
  body:JSON.stringify({
    tableNumber:"T01",
    orderType:"Dine In",
    instructions:"",
    items:cart.map(x=>({
      id:x.id,name:x.name,category:x.category,
      pricePaise:x.price*100,quantity:x.quantity,icon:x.icon
    }))
  })
});
const data=await r.json();

const checkout=new Razorpay({
  key:data.razorpayKeyId,
  amount:data.amount,
  currency:"INR",
  order_id:data.razorpayOrderId,
  name:"QROrder Restaurant",
  description:"Restaurant Order "+data.token,
  handler:async function(p){
    const v=await fetch("http://localhost:8080/api/payments/verify",{
      method:"POST",
      headers:{"Content-Type":"application/json"},
      body:JSON.stringify({
        token:data.token,
        razorpayPaymentId:p.razorpay_payment_id,
        razorpayOrderId:p.razorpay_order_id,
        razorpaySignature:p.razorpay_signature
      })
    });
    const result=await v.json();
    if(!result.success) throw new Error("Payment verification failed");
    // NOW show order confirmation.
  }
});
checkout.open();
```

## Owner page
Replace localStorage order loading with:
```js
const orders = await fetch("http://localhost:8080/api/orders").then(r=>r.json());
```

Update status:
```js
await fetch("http://localhost:8080/api/orders/"+token+"/status",{
  method:"PATCH",
  headers:{"Content-Type":"application/json"},
  body:JSON.stringify({status:"Preparing"})
});
```

Poll every 2 seconds or use WebSocket later.

## Important
For real customer phones and owner computers, serve the HTML from a real web server/domain and point API_BASE to your HTTPS backend. LocalStorage is not the database anymore.
