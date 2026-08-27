package cafe_backend.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableNumber;

    private Boolean occupied = false;

    @Column(unique = true, nullable = false)
    private String qrToken;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    public RestaurantTable() {
    }

    @PrePersist
    public void generateQrToken() {
        if (this.qrToken == null || this.qrToken.isEmpty()) {
            this.qrToken = UUID.randomUUID().toString();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }

    public Cafe getCafe() {
        return cafe;
    }

    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
    }
}