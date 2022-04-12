package de.mwa.evopiaserver;

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.WebServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EvopiaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EvopiaServerApplication.class, args);
        WebServer.INSTANCE.start();
    }
}
