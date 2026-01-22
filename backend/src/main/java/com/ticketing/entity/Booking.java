package com.ticketing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Records a successful seat booking.
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false, unique = true)
    private Seat seat;

    @Column(nullable = false)
    private LocalDateTime bookedAt;

    // Default constructor required by JPA
    public Booking() {}

    public Booking(String customerName, Seat seat) {
        this.customerName = customerName;
        this.seat = seat;
        this.bookedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}
