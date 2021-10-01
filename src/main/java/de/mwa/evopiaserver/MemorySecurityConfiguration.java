package de.mwa.evopiaserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class MemorySecurityConfiguration extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("emma")
//                    .password("emma")
//                    .roles("boss", "dev")
//                .and().passwordEncoder(new BCryptPasswordEncoder());
//    }

    private static final Log logger = LogFactory.getLog(MemorySecurityConfiguration.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/events").hasAnyRole("boss", "dev")
                .antMatchers("/events/*").hasRole("boss")
                .antMatchers("/").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager());
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        final Properties users = new Properties();
        users.put("emma","emma,boss,enabled"); //add whatever other user you need
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager(users);
        UserDetails emma = manager.loadUserByUsername("emma");
        logger.info("What about emma? " + emma.getPassword());
        return manager;
    }
}
