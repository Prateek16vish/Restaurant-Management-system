package cafe_backend.controller;


import cafe_backend.dto.OrderRequest;
import cafe_backend.dto.OrderResponse;
import cafe_backend.dto.OrderStatusRequest;
import cafe_backend.entity.Cafe;
import cafe_backend.entity.MenuItem;
import cafe_backend.entity.Order;
import cafe_backend.entity.OrderItem;
import cafe_backend.entity.RestaurantTable;
import cafe_backend.repository.CafeRepository;
import cafe_backend.repository.MenuItemRepository;
import cafe_backend.repository.OrderItemRepository;
import cafe_backend.repository.OrderRepository;
import cafe_backend.repository.RestaurantTableRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;
    //private final CafeRepository cafeRepository;

    public OrderController(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            RestaurantTableRepository tableRepository,
            MenuItemRepository menuItemRepository,
            CafeRepository cafeRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
        //this.cafeRepository = cafeRepository;
    }

    // CREATE ORDER
    @PostMapping
    public Order createOrder(@RequestBody OrderRequest request) {

        // Validate table
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));

        // Validate cafe
        Cafe cafe = table.getCafe();

        if (cafe == null) {
            throw new RuntimeException("Table is not assigned to a cafe");
        }

        // Validate order items
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        // Create Order
        Order order = new Order();

        order.setTable(table);
        order.setCafe(cafe);
        order.setOrderType(request.getOrderType());
        order.setStatus("PENDING");

        double totalAmount = 0;

        List<OrderItem> orderItems = new ArrayList<>();

        // Process each item
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {

            if (itemRequest.getQuantity() == null ||
                    itemRequest.getQuantity() <= 0) {

                throw new RuntimeException("Quantity must be greater than zero");
            }

            MenuItem menuItem = menuItemRepository
                    .findById(itemRequest.getMenuItemId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Menu item not found: "
                                            + itemRequest.getMenuItemId()
                            ));

            // Don't allow unavailable items
            if (Boolean.FALSE.equals(menuItem.getAvailable())) {
                throw new RuntimeException(
                        "Menu item is currently unavailable: "
                                + menuItem.getName()
                );
            }

            // Get price from DATABASE
            double price = menuItem.getPrice();

            int quantity = itemRequest.getQuantity();

            totalAmount += price * quantity;

            // Create OrderItem
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(quantity);

            // Save price snapshot
            orderItem.setPrice(price);

            orderItems.add(orderItem);
        }

        // Set calculated total
        order.setTotalAmount(totalAmount);

        // Save Order first
        Order savedOrder = orderRepository.save(order);

        // Save Order Items
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);
        }

        orderItemRepository.saveAll(orderItems);

        return savedOrder;
    }

    // GET ALL ORDERS
    @GetMapping
    public List<OrderResponse> getAllOrders() {

        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {

                    List<OrderItem> items =
                            orderItemRepository.findByOrderId(order.getId());

                    return new OrderResponse(order, items);

                })
                .toList();
    }

    // GET ORDER BY ID
    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        return new OrderResponse(order, items);
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusRequest request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        String newStatus = request.getStatus();

        if (newStatus == null || newStatus.isBlank()) {
            throw new RuntimeException("Status is required");
        }

        newStatus = newStatus.toUpperCase();

        if (!newStatus.equals("PENDING")
                && !newStatus.equals("PREPARING")
                && !newStatus.equals("READY")
                && !newStatus.equals("COMPLETED")) {

            throw new RuntimeException(
                    "Invalid status. Use PENDING, PREPARING, READY or COMPLETED"
            );
        }

        order.setStatus(newStatus);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> items =
                orderItemRepository.findByOrderId(savedOrder.getId());

        return new OrderResponse(savedOrder, items);
    }
}