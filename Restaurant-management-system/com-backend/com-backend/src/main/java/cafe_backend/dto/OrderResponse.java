package cafe_backend.dto;

import cafe_backend.entity.Order;
import cafe_backend.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private String tableNumber;
    private String orderType;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private List<ItemResponse> items;

    public OrderResponse() {
    }

    public OrderResponse(
            Order order,
            List<OrderItem> orderItems) {

        this.id = order.getId();

        if (order.getTable() != null) {
            this.tableNumber = order.getTable().getTableNumber();
        }

        this.orderType = order.getOrderType();
        this.status = order.getStatus();
        this.totalAmount = order.getTotalAmount();
        this.createdAt = order.getCreatedAt();

        this.items = orderItems.stream()
                .map(ItemResponse::new)
                .toList();
    }

    public Long getId() {
        return id;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getStatus() {
        return status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<ItemResponse> getItems() {
        return items;
    }

    public static class ItemResponse {

        private Long menuItemId;
        private String name;
        private Integer quantity;
        private Double price;
        private Double subtotal;

        public ItemResponse(OrderItem item) {

            this.menuItemId = item.getMenuItem().getId();
            this.name = item.getMenuItem().getName();
            this.quantity = item.getQuantity();
            this.price = item.getPrice();

            this.subtotal =
                    item.getPrice() * item.getQuantity();
        }

        public Long getMenuItemId() {
            return menuItemId;
        }

        public String getName() {
            return name;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public Double getPrice() {
            return price;
        }

        public Double getSubtotal() {
            return subtotal;
        }
    }
}