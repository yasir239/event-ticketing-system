import { Link } from 'react-router-dom';
import './EventCard.css';

export default function EventCard({ event }) {
    const formattedDate = new Date(event.eventDate).toLocaleDateString('en-US', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });

    // Calculate available seats from the seats array
    const totalSeats = event.seats?.length || 0;
    const bookedSeats = event.seats?.filter(s => s.booked).length || 0;
    const availableSeats = totalSeats - bookedSeats;

    return (
        <Link to={`/event/${event.id}`} className="event-card">
            <div className="event-image">
                <img src="/knight-seven-kingdoms.png" alt={event.name} className="event-image-img" />
                <div className="event-badge">{availableSeats} seats left</div>
            </div>
            <div className="event-content">
                <h3 className="event-title">{event.name}</h3>
                <p className="event-venue">{event.venue}</p>
                <p className="event-date">{formattedDate}</p>
                <div className="event-footer">
                    <span className="event-price">Book Now</span>
                    <span className="event-arrow">→</span>
                </div>
            </div>
        </Link>
    );
}
