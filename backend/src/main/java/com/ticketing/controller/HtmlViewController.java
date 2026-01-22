package com.ticketing.controller;

import com.ticketing.entity.Event;
import com.ticketing.entity.Seat;
import com.ticketing.repository.EventRepository;
import com.ticketing.repository.SeatRepository;
import com.ticketing.util.HtmlJsonRenderer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides styled HTML views of API data for browser access.
 * The actual API endpoints in EventController continue to return JSON for
 * applications.
 */
@Controller
@RequestMapping("/view")
public class HtmlViewController {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public HtmlViewController(EventRepository eventRepository, SeatRepository seatRepository) {
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String viewAllEvents() {
        List<Event> events = eventRepository.findAll();
        return HtmlJsonRenderer.render("All Events", events);
    }

    @GetMapping(value = "/events/{eventId}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String viewEvent(@PathVariable Long eventId) {
        return eventRepository.findById(eventId)
                .map(event -> HtmlJsonRenderer.render("Event: " + event.getName(), event))
                .orElse(HtmlJsonRenderer.render("Error", "Event not found"));
    }

    @GetMapping(value = "/events/{eventId}/seats", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String viewSeatsForEvent(@PathVariable Long eventId) {
        List<Seat> seats = seatRepository.findByEventId(eventId);
        return HtmlJsonRenderer.render("Seats for Event " + eventId, seats);
    }
}
