const API_BASE = 'http://localhost:8080/api';

/**
 * Fetch all events
 */
export async function getEvents() {
    const response = await fetch(`${API_BASE}/events`);
    if (!response.ok) {
        throw new Error('Failed to fetch events');
    }
    return response.json();
}

/**
 * Fetch all seats for an event
 */
export async function getSeats(eventId) {
    const response = await fetch(`${API_BASE}/events/${eventId}/seats`);
    if (!response.ok) {
        throw new Error('Failed to fetch seats');
    }
    return response.json();
}

/**
 * Book a seat
 * Returns the booking on success, or throws an error with details on failure.
 */
export async function bookSeat(seatId, customerName) {
    const response = await fetch(
        `${API_BASE}/seats/${seatId}/book?customerName=${encodeURIComponent(customerName)}`,
        { method: 'POST' }
    );

    const data = await response.json();

    if (!response.ok) {
        // This includes 409 Conflict from concurrent booking
        const error = new Error(data.message || 'Booking failed');
        error.status = response.status;
        error.data = data;
        throw error;
    }

    return data;
}

/**
 * Simulate concurrent booking - sends two requests simultaneously
 * This demonstrates the optimistic locking behavior
 */
export async function simulateConcurrentBooking(seatId) {
    const results = await Promise.allSettled([
        bookSeat(seatId, 'User_A'),
        bookSeat(seatId, 'User_B')
    ]);

    return {
        request1: results[0],
        request2: results[1]
    };
}
