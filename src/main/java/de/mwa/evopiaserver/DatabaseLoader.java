package de.mwa.evopiaserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class DatabaseLoader {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseLoader.class);

//    @Bean
    CommandLineRunner initDatabase(EventRepository repo) {
        return args -> {
//            repo.save(new Event("name", "description", "date", "time", "place", "tag1,tag2"));
//            repo.save(new Event("Prayer meeting", "Let's all pray together", "Wednesdays", "08:00 AM", "Zoom", "Prayer,Online"));
//
//            repo.findAll().forEach(event -> LOG.info("Preloaded: " + event));
        };
    }
}
