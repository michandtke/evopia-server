package de.mwa.evopiaserver;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EventAssemblyManager implements RepresentationModelAssembler<Event, EntityModel<Event>> {
    @Override
    public EntityModel<Event> toModel(Event entity) {
        return EntityModel.of(entity);
    }
}
