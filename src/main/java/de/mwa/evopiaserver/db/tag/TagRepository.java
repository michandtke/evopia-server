package de.mwa.evopiaserver.db.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Collection<Tag> findByNameIn(Collection<String> names);
}
