package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.dto.EventDto;
import de.mwa.evopiaserver.db.kotlin.EventRepositoryNew;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {
    private final EventRepositoryNew eventRepository;

    public EventController(EventRepositoryNew eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/v2/events")
    public List<EventDto> getEvents() {
        return eventRepository.events();
    }

    @PostMapping("/v2/events/upsert")
    public String upsertEvent(@RequestBody EventDto eventDto) {
        eventRepository.upsert(eventDto);
        return "Upserted Event: " + eventDto;
    }
}
