package de.mwa.evopiaserver.db.channel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> deleteByName(String name);

    Collection<Channel> findByNameIn(Collection<String> names);
}
