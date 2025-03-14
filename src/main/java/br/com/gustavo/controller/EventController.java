package br.com.gustavo.controller;

import br.com.gustavo.domain.event.Event;
import br.com.gustavo.domain.event.EventRequestDTO;
import br.com.gustavo.domain.event.EventResponseDTO;
import br.com.gustavo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


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
}
