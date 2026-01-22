package com.ticketing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Represents a seat at an event.
 * 
 * KEY CONCEPT: The @Version annotation enables Optimistic Locking.
 * 
 * How it works:
 * 1. When a seat is loaded, its current version number is stored.
 * 2. When saving, JPA adds "WHERE version = [loaded_version]" to the UPDATE.
 * 3. If another transaction already updated (and incremented version), 
 *    the WHERE clause finds 0 rows, and JPA throws OptimisticLockException.
 * 4. This prevents two users from booking the same seat simultaneously.
 */
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNumber;  // e.g., "A1", "B5"

    @Column(nullable = false)
    private boolean booked = false;

    /**
     * VERSION FIELD - The heart of Optimistic Locking.
     * 
     * JPA automatically:
     * - Reads this value when loading the entity
     * - Increments it on every UPDATE
     * - Checks it hasn't changed before committing
     */
    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore  // Prevent infinite recursion in JSON
    private Event event;

    // Default constructor required by JPA
    public Seat() {}

    public Seat(String seatNumber, Event event) {
        this.seatNumber = seatNumber;
        this.event = event;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
