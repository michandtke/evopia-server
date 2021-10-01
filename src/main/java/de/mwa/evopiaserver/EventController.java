package de.mwa.evopiaserver;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//@RestController
@CrossOrigin
public class EventController {

    private final EventRepository eventRepository;
    private final EventAssemblyManager eventAssembly;

    public EventController(EventRepository eventRepository, EventAssemblyManager eventAssembly) {
        this.eventRepository = eventRepository;
        this.eventAssembly = eventAssembly;
    }

    @GetMapping("/events/{id}")
    public EntityModel<Event> oneEvent(@PathVariable Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        return eventAssembly.toModel(event);
    }

    @GetMapping("/events")
    public CollectionModel<EntityModel<Event>> all() {
        List<EntityModel<Event>> events = eventRepository.findAll().stream().map(eventAssembly::toModel).collect(Collectors.toList());
        return CollectionModel.of(events, linkTo(methodOn(EventController.class).all()).withSelfRel());
    }
}
