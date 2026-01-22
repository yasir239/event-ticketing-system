package com.ticketing.exception;

/**
 * Thrown when a concurrent booking collision is detected.
 * 
 * This happens when the @Version check fails - meaning another
 * transaction modified the seat between when we read it and
 * when we tried to save it.
 */
public class ConcurrentBookingException extends RuntimeException {

    public ConcurrentBookingException(String message) {
        super(message);
    }

    public ConcurrentBookingException(Long seatId) {
        super("Booking conflict for seat " + seatId +
                ". Another user just booked this seat. Please try another seat.");
    }
}
