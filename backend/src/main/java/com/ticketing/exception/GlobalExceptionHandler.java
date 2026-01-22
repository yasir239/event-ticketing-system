package com.ticketing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that catches all exceptions and returns
 * clean, consistent error responses.
 * 
 * KEY: We catch ObjectOptimisticLockingFailureException here.
 * This is the exception JPA throws when the @Version check fails.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles optimistic locking failures.
     * 
     * This is triggered when two users try to book the same seat simultaneously.
     * The first one succeeds, the second one gets this 409 Conflict response.
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLockException(
            ObjectOptimisticLockingFailureException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Booking Conflict");
        error.put("message", "This seat was just booked by another user. Please select a different seat.");
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("technicalDetails", "Optimistic lock failure - version mismatch");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(SeatAlreadyBookedException.class)
    public ResponseEntity<Map<String, Object>> handleSeatAlreadyBooked(
            SeatAlreadyBookedException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Seat Unavailable");
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ConcurrentBookingException.class)
    public ResponseEntity<Map<String, Object>> handleConcurrentBooking(
            ConcurrentBookingException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Concurrent Booking Detected");
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
