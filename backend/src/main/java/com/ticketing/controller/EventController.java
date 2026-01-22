package com.ticketing.controller;

import com.ticketing.entity.Event;
import com.ticketing.entity.Seat;
import com.ticketing.repository.EventRepository;
import com.ticketing.repository.SeatRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173") // Allow React dev server
public class EventController {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public EventController(EventRepository eventRepository, SeatRepository seatRepository) {
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        return eventRepository.findById(eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{eventId}/seats")
    public List<Seat> getSeatsForEvent(@PathVariable Long eventId) {
        return seatRepository.findByEventId(eventId);
    }
}
