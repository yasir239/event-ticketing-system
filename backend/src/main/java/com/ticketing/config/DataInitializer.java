package com.ticketing.config;

import com.ticketing.entity.Event;
import com.ticketing.entity.Seat;
import com.ticketing.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Seeds the database with sample data on application startup.
 * Creates a sample event with a grid of seats for testing.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;

    public DataInitializer(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        // If database is empty, seed it
        if (eventRepository.count() == 0) {
            // Create a sample event
            Event concert = new Event(
                    "A Knight of the Seven Kingdoms",
                    LocalDateTime.of(2026, 3, 15, 19, 0),
                    "HBO Max Theater");

            // Create a 5x6 grid of seats (30 seats total)
            String[] rows = { "A", "B", "C", "D", "E" };
            for (String row : rows) {
                for (int num = 1; num <= 6; num++) {
                    Seat seat = new Seat(row + num, concert);
                    concert.addSeat(seat);
                }
            }

            eventRepository.save(concert);
            System.out.println("✅ Database seeded with event: " + concert.getName());
        } else {
            // Update existing event for the demo if it's the old name
            eventRepository.findAll().stream().findFirst().ifPresent(event -> {
                if (event.getName().equals("Tech Conference 2026")) {
                    event.setName("A Knight of the Seven Kingdoms");
                    event.setVenue("HBO Max Theater");
                    eventRepository.save(event);
                    System.out.println("✅ Updated existing event to: " + event.getName());
                }
            });
        }
    }
}
