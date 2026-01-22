import Seat from './Seat';
import './SeatGrid.css';

/**
 * Displays a grid of seats for the venue
 * Seats are organized by row (extracted from seatNumber like "A1", "B3")
 */
export default function SeatGrid({ seats, onBook, bookingInProgress }) {
    // Group seats by row letter (A, B, C, etc.)
    const seatsByRow = seats.reduce((acc, seat) => {
        const row = seat.seatNumber.charAt(0);
        if (!acc[row]) acc[row] = [];
        acc[row].push(seat);
        return acc;
    }, {});

    // Sort rows alphabetically
    const sortedRows = Object.keys(seatsByRow).sort();

    return (
        <div className="seat-grid-container">
            {/* Stage indicator */}
            <div className="stage">
                <span>STAGE</span>
            </div>

            {/* Seat grid */}
            <div className="seat-grid">
                {sortedRows.map(row => (
                    <div key={row} className="seat-row">
                        <span className="row-label">{row}</span>
                        <div className="row-seats">
                            {seatsByRow[row]
                                .sort((a, b) => {
                                    const numA = parseInt(a.seatNumber.slice(1));
                                    const numB = parseInt(b.seatNumber.slice(1));
                                    return numA - numB;
                                })
                                .map(seat => (
                                    <Seat
                                        key={seat.id}
                                        seat={seat}
                                        onBook={onBook}
                                        isBooking={bookingInProgress === seat.id}
                                    />
                                ))}
                        </div>
                    </div>
                ))}
            </div>

            {/* Legend */}
            <div className="legend">
                <div className="legend-item">
                    <div className="legend-color available"></div>
                    <span>Available</span>
                </div>
                <div className="legend-item">
                    <div className="legend-color booked"></div>
                    <span>Booked</span>
                </div>
                <div className="legend-item">
                    <div className="legend-color booking"></div>
                    <span>Booking...</span>
                </div>
            </div>
        </div>
    );
}
