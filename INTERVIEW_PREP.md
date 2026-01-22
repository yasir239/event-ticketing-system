# 🎯 Interview Prep: Explaining Optimistic Locking

## The 30-Second Elevator Pitch

> "To prevent double-booking, I used Optimistic Locking with JPA's `@Version` annotation. Each seat has a version number. When updating, the database checks if the version still matches. If another user already booked it, the version won't match, and my transaction fails gracefully with a 409 Conflict."

---

## Key Questions & Answers

### Q: "How did you prevent two users from booking the same seat at the same time?"

**Answer:**
"I used Optimistic Locking. Every seat has a `version` field annotated with JPA's `@Version`. When I try to update a seat:

1. My app reads the seat with version 0
2. Another user's app also reads it with version 0
3. Their update goes first: `UPDATE seats SET booked=true, version=1 WHERE id=5 AND version=0` ✅ Works (1 row updated)
4. My update tries: `UPDATE seats SET booked=true, version=1 WHERE id=5 AND version=0` ❌ Fails (0 rows - version is now 1)

The database returns 0 rows updated, JPA throws an `OptimisticLockException`, and I return a 409 Conflict to the user."

---

### Q: "Why Optimistic Locking instead of Pessimistic Locking?"

**Answer:**
"Pessimistic locking uses `SELECT FOR UPDATE` which holds a database lock until the transaction completes. This:
- Blocks other readers
- Reduces throughput
- Can cause deadlocks

Optimistic locking assumes conflicts are rare. It doesn't block anyone - instead, it detects conflicts at write time. For a ticketing system where most seats are available and conflicts are uncommon, this gives better performance."

---

### Q: "What's the trade-off?"

**Answer:**
"The trade-off is that with optimistic locking, the losing user has to retry. In high-contention scenarios (like 1000 people trying to book the last seat), you'd get many retries. For those cases, pessimistic locking or a queue-based system might be better."

---

### Q: "Walk me through your code."

**Answer:**
"Here's the key parts:

1. **Entity** - The `Seat` class has a `@Version` field:
```java
@Version
private Long version;
```

2. **Service** - The `BookingService.reserveSeat()` method:
```java
@Transactional
public Booking reserveSeat(Long seatId, String customerName) {
    Seat seat = seatRepository.findById(seatId);  // JPA remembers version
    seat.setBooked(true);
    seatRepository.save(seat);  // Version check happens here
    return bookingRepository.save(new Booking(customerName, seat));
}
```

3. **Exception Handler** - Catches the exception and returns 409:
```java
@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
public ResponseEntity<?> handleConflict(Exception ex) {
    return ResponseEntity.status(409).body("Seat was just booked by another user");
}
```

The beauty is that JPA handles all the version checking automatically. I just add the annotation."

---

### Q: "How would you test this?"

**Answer:**
"I built a 'Simulate Concurrent Booking' feature in the frontend. It sends two booking requests for the same seat simultaneously using `Promise.allSettled()`. You can visually see one succeed and one fail with a 409 error. This proves the optimistic lock is working."

---

### Q: "What would you do differently at scale?"

**Answer:**
"At high scale, I might:
1. **Add retry logic** with exponential backoff for transient failures
2. **Use a distributed lock** (Redis) for high-contention resources
3. **Implement a queue** (RabbitMQ) for processing booking requests
4. **Consider event sourcing** for complete audit trails

But for an MVP demonstrating the concept, optimistic locking is the right level of complexity."

---

## The Generated SQL

When you call `seatRepository.save(seat)`, Hibernate generates:

```sql
UPDATE seats 
SET booked = true, version = version + 1 
WHERE id = 5 AND version = 0
```

If the version has changed (another transaction committed first), this returns "0 rows updated" and Hibernate throws `ObjectOptimisticLockingFailureException`.

---

## Diagram to Draw on Whiteboard

```
User A                    User B                    Database
  |                         |                          |
  |------- Read Seat ------>|                          |
  |       (version=0)       |                          |
  |                         |------- Read Seat ------->|
  |                         |       (version=0)        |
  |                         |                          |
  |--- UPDATE (version=0) --|------------------------->|
  |       ✅ SUCCESS                                   | version now = 1
  |                         |                          |
  |                         |--- UPDATE (version=0) -->|
  |                         |       ❌ FAILS           | version mismatch!
  |                         |                          |
```

---

## Project Structure to Mention

```
backend/
├── entity/Seat.java       ← @Version annotation here
├── service/BookingService.java  ← Business logic
├── exception/GlobalExceptionHandler.java  ← Catches OptimisticLockException
└── controller/BookingController.java  ← REST API

frontend/
└── src/App.jsx  ← Concurrent booking simulation
```

---

## Key Vocabulary

- **Optimistic Locking**: Assume no conflicts, check at save time
- **Pessimistic Locking**: Lock the row immediately, block others
- **@Version**: JPA annotation that automatically manages version
- **ObjectOptimisticLockingFailureException**: Spring's wrapper for the JPA exception
- **409 Conflict**: HTTP status code for resource conflicts
- **MVCC (Multi-Version Concurrency Control)**: PostgreSQL's underlying mechanism

---

*Good luck with your interview! 🚀*
