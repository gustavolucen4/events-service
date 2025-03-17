package br.com.gustavo.controller;

import br.com.gustavo.domain.event.Event;
import br.com.gustavo.domain.event.EventDetailsResponseDTO;
import br.com.gustavo.domain.event.EventRequestDTO;
import br.com.gustavo.domain.event.EventResponseDTO;
import br.com.gustavo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> crete(@RequestParam("title") String title,
                                       @RequestParam(value = "description", required = false) String description,
                                       @RequestParam("date") Long date,
                                       @RequestParam("city") String city,
                                       @RequestParam("state") String state,
                                       @RequestParam("remote") Boolean remote,
                                       @RequestParam("eventUrl") String eventUrl,
                                       @RequestParam("image") MultipartFile image){

        EventRequestDTO request = new EventRequestDTO(title, description, date, city, state, remote, eventUrl, image);
        Event newEvent = eventService.createEvent(request);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){

        List<EventResponseDTO> pageEvents = eventService.getUpcomingEvents(page, size);
        return  ResponseEntity.ok(pageEvents);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsResponseDTO> getEventById(@RequestParam("eventId") UUID eventId){

        EventDetailsResponseDTO pageEvents = eventService.getEventById(eventId);
        return  ResponseEntity.ok(pageEvents);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> getFilteredEvents(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(value = "title", required = false) String title,
                                                                  @RequestParam(value = "city", required = false) String city,
                                                                  @RequestParam(value = "state", required = false) String state,
                                                                  @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
                                                                  @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate){

        List<EventResponseDTO> pageEvents = eventService.getFilteredEvents(page, size, title, city, state, startDate, endDate);
        return  ResponseEntity.ok(pageEvents);
    }

}