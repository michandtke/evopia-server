package de.mwa.evopiaserver;

import de.mwa.evopiaserver.registration.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@SpringBootApplication
public class EvopiaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EvopiaServerApplication.class, args);
    }
}
