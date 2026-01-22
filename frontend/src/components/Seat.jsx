import './Seat.css';

/**
 * Individual seat component
 * Green = Available, Red = Booked, Yellow = Currently being booked
 */
export default function Seat({ seat, onBook, isBooking }) {
    const isAvailable = !seat.booked && !isBooking;

    const handleClick = () => {
        if (isAvailable) {
            onBook(seat);
        }
    };

    return (
        <button
            className={`seat ${seat.booked ? 'booked' : isBooking ? 'booking' : 'available'}`}
            onClick={handleClick}
            disabled={!isAvailable}
            title={seat.booked ? 'Booked' : isBooking ? 'Booking...' : `Book seat ${seat.seatNumber}`}
        >
            {seat.seatNumber}
        </button>
    );
}
