package com.ticketing.exception;

/**
 * Thrown when a user tries to book a seat that is already booked.
 * This is a business logic exception for when the seat was already
 * marked as booked before the concurrent access check.
 */
public class SeatAlreadyBookedException extends RuntimeException {

    public SeatAlreadyBookedException(String message) {
        super(message);
    }

    public SeatAlreadyBookedException(Long seatId) {
        super("Seat " + seatId + " is already booked");
    }
}
