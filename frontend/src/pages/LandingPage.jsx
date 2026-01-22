import { useState, useEffect } from 'react';
import EventCard from '../components/EventCard';
import { getEvents } from '../api/bookingApi';
import './LandingPage.css';

export default function LandingPage() {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchEvents();
    }, []);

    const fetchEvents = async () => {
        try {
            setLoading(true);
            const data = await getEvents();
            setEvents(data);
            setError(null);
        } catch (err) {
            setError('Failed to load events. Make sure the backend is running.');
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="landing-page">
                <div className="loading-state">Loading events...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="landing-page">
                <div className="error-state">
                    <h2>Connection Error</h2>
                    <p>{error}</p>
                    <button onClick={fetchEvents} className="retry-btn">Retry</button>
                </div>
            </div>
        );
    }

    return (
        <div className="landing-page">
            {/* Hero Section */}
            <section className="hero">
                <div className="hero-content">
                    <h1>Experience Live Events</h1>
                    <p>Book your seats instantly with real-time availability</p>
                </div>
                <div className="hero-badge">
                    <span className="badge-icon">⚡</span>
                    <span>Real-time Seat Booking</span>
                </div>
            </section>

            {/* Events Grid */}
            <section className="events-section">
                <h2 className="section-title">Now Showing</h2>
                <div className="events-grid">
                    {events.map(event => (
                        <EventCard key={event.id} event={event} />
                    ))}
                </div>
            </section>

            {/* Tech Demo Section */}
            <section className="tech-section">
                <div className="tech-content">
                    <h2>Built for Scale</h2>
                    <p>This demo showcases handling concurrent seat bookings using JPA @Version for optimistic locking — preventing double-booking race conditions.</p>
                    <div className="tech-stack">
                        <span className="tech-tag">Spring Boot 3</span>
                        <span className="tech-tag">React 18</span>
                        <span className="tech-tag">PostgreSQL</span>
                        <span className="tech-tag">JPA</span>
                    </div>
                </div>
            </section>
        </div>
    );
}
