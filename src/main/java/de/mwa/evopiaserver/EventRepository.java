package de.mwa.evopiaserver;

import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Long> { }
