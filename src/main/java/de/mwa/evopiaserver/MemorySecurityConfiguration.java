package de.mwa.evopiaserver;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class MemorySecurityConfiguration extends WebSecurityConfigurerAdapter {
    protected MemorySecurityConfiguration() {
        super();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("emma")
                    .password("emma")
                    .roles("boss", "dev")
                .and()
                .withUser("chris")
                    .roles("dev");
    }
}
