package com.ticketing.controller;

import com.ticketing.entity.Booking;
import com.ticketing.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for booking operations.
 * 
 * The key endpoint is POST /api/seats/{seatId}/book
 * This triggers the optimistic locking logic in BookingService.
 */
@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Book a seat.
     * 
     * If two requests come in simultaneously for the same seat:
     * - One will succeed (returns 200 with booking details)
     * - One will fail (returns 409 Conflict)
     * 
     * @param seatId       The seat to book
     * @param customerName The customer's name
     * @return The booking details, or a 409 error
     */
    @PostMapping("/{seatId}/book")
    public ResponseEntity<Booking> bookSeat(
            @PathVariable Long seatId,
            @RequestParam String customerName) {

        Booking booking = bookingService.reserveSeat(seatId, customerName);
        return ResponseEntity.ok(booking);
    }
}
