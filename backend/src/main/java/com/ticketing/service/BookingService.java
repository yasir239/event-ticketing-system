package com.ticketing.service;

import com.ticketing.entity.Booking;
import com.ticketing.entity.Seat;
import com.ticketing.exception.SeatAlreadyBookedException;
import com.ticketing.repository.BookingRepository;
import com.ticketing.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * THE CORE SERVICE - This is where Optimistic Locking magic happens.
 * 
 * Interview explanation:
 * "When reserveSeat() is called, JPA loads the Seat with its current version.
 * When we call save(), JPA generates an UPDATE with WHERE version = ?
 * If another transaction already incremented the version, the WHERE finds 0
 * rows,
 * and JPA throws ObjectOptimisticLockingFailureException."
 */
@Service
public class BookingService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    public BookingService(SeatRepository seatRepository, BookingRepository bookingRepository) {
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Reserves a seat for a customer.
     * 
     * THE RACE CONDITION SCENARIO:
     * 
     * Timeline (two users booking same seat):
     * 
     * T1: User A calls reserveSeat(seatId=5)
     * T2: User B calls reserveSeat(seatId=5)
     * T3: User A's transaction loads Seat #5 (version=0, booked=false)
     * T4: User B's transaction loads Seat #5 (version=0, booked=false)
     * T5: User A sets booked=true, saves → UPDATE seats SET booked=true, version=1
     * WHERE id=5 AND version=0
     * → Success! 1 row updated. Version is now 1.
     * T6: User B sets booked=true, saves → UPDATE seats SET booked=true, version=1
     * WHERE id=5 AND version=0
     * → Fails! 0 rows updated (version is no longer 0). JPA throws
     * OptimisticLockException.
     * 
     * Result: User A gets the seat, User B gets a 409 Conflict error.
     * 
     * @param seatId       The ID of the seat to book
     * @param customerName The name of the customer
     * @return The created Booking
     * @throws SeatAlreadyBookedException                                      if
     *                                                                         seat
     *                                                                         is
     *                                                                         already
     *                                                                         booked
     * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if
     *                                                                         concurrent
     *                                                                         booking
     *                                                                         detected
     */
    @Transactional
    public Booking reserveSeat(Long seatId, String customerName) {
        // Step 1: Load the seat (JPA remembers the version number)
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));

        // Step 2: Business logic check - is it already booked?
        if (seat.isBooked()) {
            throw new SeatAlreadyBookedException(seatId);
        }

        // Step 3: Mark as booked
        seat.setBooked(true);

        // Step 4: Save the seat - THIS IS WHERE OPTIMISTIC LOCKING KICKS IN
        // JPA will generate: UPDATE seats SET booked=true, version=version+1 WHERE id=?
        // AND version=?
        // If version doesn't match, 0 rows are updated → OptimisticLockException
        seatRepository.save(seat);

        // Step 5: Create and save the booking record
        Booking booking = new Booking(customerName, seat);
        return bookingRepository.save(booking);
    }
}
