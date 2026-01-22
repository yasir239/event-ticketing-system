import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import SeatGrid from '../components/SeatGrid';
import { getSeats, bookSeat, simulateConcurrentBooking } from '../api/bookingApi';
import './BookingPage.css';

export default function BookingPage() {
    const { eventId } = useParams();
    const [seats, setSeats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [notification, setNotification] = useState(null);
    const [bookingInProgress, setBookingInProgress] = useState(null);
    const [concurrentResult, setConcurrentResult] = useState(null);

    useEffect(() => {
        fetchSeats();
    }, [eventId]);

    const fetchSeats = async () => {
        try {
            setLoading(true);
            const data = await getSeats(eventId);
            setSeats(data);
            setError(null);
        } catch (err) {
            setError('Failed to load seats. Make sure the backend is running.');
        } finally {
            setLoading(false);
        }
    };

    const handleBookSeat = async (seat) => {
        const customerName = prompt('Enter your name to book this seat:');
        if (!customerName) return;

        setBookingInProgress(seat.id);
        setNotification(null);

        try {
            await bookSeat(seat.id, customerName);
            setNotification({
                type: 'success',
                message: `Seat ${seat.seatNumber} booked successfully for ${customerName}!`
            });
            fetchSeats();
        } catch (err) {
            setNotification({
                type: 'error',
                message: err.data?.message || `Failed to book seat: ${err.message}`
            });
        } finally {
            setBookingInProgress(null);
        }
    };

    const handleSimulateConcurrent = async () => {
        const availableSeat = seats.find(s => !s.booked);
        if (!availableSeat) {
            setNotification({
                type: 'error',
                message: 'No available seats to demonstrate.'
            });
            return;
        }

        setBookingInProgress(availableSeat.id);
        setNotification({
            type: 'info',
            message: `Testing concurrent booking on seat ${availableSeat.seatNumber}...`
        });
        setConcurrentResult(null);

        try {
            const result = await simulateConcurrentBooking(availableSeat.id);

            setConcurrentResult({
                seat: availableSeat.seatNumber,
                userA: result.request1.status === 'fulfilled' ? 'SUCCESS' : 'CONFLICT',
                userB: result.request2.status === 'fulfilled' ? 'SUCCESS' : 'CONFLICT',
                userADetail: result.request1.status === 'fulfilled'
                    ? 'Booking confirmed'
                    : result.request1.reason?.data?.message || 'Optimistic lock failure',
                userBDetail: result.request2.status === 'fulfilled'
                    ? 'Booking confirmed'
                    : result.request2.reason?.data?.message || 'Optimistic lock failure'
            });

            setNotification({
                type: 'success',
                message: 'Race condition test complete! See results below.'
            });

            fetchSeats();
        } catch (err) {
            setNotification({
                type: 'error',
                message: `Simulation failed: ${err.message}`
            });
        } finally {
            setBookingInProgress(null);
        }
    };

    if (loading) {
        return (
            <div className="booking-page">
                <div className="loading-state">Loading seats...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="booking-page">
                <div className="error-state">
                    <h2>Connection Error</h2>
                    <p>{error}</p>
                    <button onClick={fetchSeats} className="retry-btn">Retry</button>
                </div>
            </div>
        );
    }

    const availableCount = seats.filter(s => !s.booked).length;
    const bookedCount = seats.filter(s => s.booked).length;

    return (
        <div className="booking-page">
            <div className="booking-header">
                <h1>Select Your Seats</h1>
                <div className="stats">
                    <div className="stat">
                        <span className="stat-value available">{availableCount}</span>
                        <span className="stat-label">Available</span>
                    </div>
                    <div className="stat">
                        <span className="stat-value booked">{bookedCount}</span>
                        <span className="stat-label">Booked</span>
                    </div>
                </div>
            </div>

            {notification && (
                <div className={`notification ${notification.type}`}>
                    {notification.message}
                </div>
            )}

            <div className="booking-content">
                <div className="seat-section">
                    <SeatGrid
                        seats={seats}
                        onBook={handleBookSeat}
                        bookingInProgress={bookingInProgress}
                    />
                </div>

                <div className="demo-section">
                    <div className="demo-card">
                        <h2>Concurrency Test</h2>
                        <p>
                            Simulate two users trying to book the same seat simultaneously.
                            One will succeed, one will get a conflict error.
                        </p>
                        <button
                            className="demo-btn"
                            onClick={handleSimulateConcurrent}
                            disabled={bookingInProgress || availableCount === 0}
                        >
                            {bookingInProgress ? 'Testing...' : 'Run Race Condition Test'}
                        </button>

                        {concurrentResult && (
                            <div className="concurrent-result">
                                <h3>Results for Seat {concurrentResult.seat}</h3>
                                <div className="result-grid">
                                    <div className="result-item">
                                        <strong>User A</strong>
                                        <span className={concurrentResult.userA === 'SUCCESS' ? 'success' : 'error'}>
                                            {concurrentResult.userA}
                                        </span>
                                        <small>{concurrentResult.userADetail}</small>
                                    </div>
                                    <div className="result-item">
                                        <strong>User B</strong>
                                        <span className={concurrentResult.userB === 'SUCCESS' ? 'success' : 'error'}>
                                            {concurrentResult.userB}
                                        </span>
                                        <small>{concurrentResult.userBDetail}</small>
                                    </div>
                                </div>
                                <p className="explanation">
                                    <strong>What happened:</strong> Both requests read the same seat version.
                                    The first UPDATE succeeded and incremented the version.
                                    The second UPDATE failed because the version no longer matched.
                                </p>
                            </div>
                        )}
                    </div>
                </div>
            </div>

            <button className="refresh-btn" onClick={fetchSeats}>
                Refresh Seats
            </button>
        </div>
    );
}
