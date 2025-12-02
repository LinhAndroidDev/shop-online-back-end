package com.example.shop.entity;

import com.example.shop.model.ShipmentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shipment")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "tracking_code")
    private String trackingCode;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @CreationTimestamp
    @Column(name = "shipped_at", updatable = false)
    private LocalDateTime shippedAt;
}
