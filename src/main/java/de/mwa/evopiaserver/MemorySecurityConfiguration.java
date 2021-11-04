package de.mwa.evopiaserver;

import de.mwa.evopiaserver.registration.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class MemorySecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailsService userDetailsService;


    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().and().authorizeRequests()
//                .antMatchers("/login*", "/logout*", "/signin/**", "/signup/**", "/customLogin",
//                        "/user/registration*", "/registrationConfirm*", "/expiredAccount*", "/registration*",
//                        "/badUser*", "/user/resendRegistrationToken*" ,"/forgetPassword*", "/user/resetPassword*","/user/savePassword*","/updatePassword*",
//                        "/user/changePassword*", "/emailError*", "/resources/**","/old/user/registration*","/successRegister*","/qrcode*","/user/enableNewLoc*",
//                        "/user/registration*").permitAll()
//                .antMatchers("/events").hasAnyRole("boss", "dev")
//                .antMatchers("/events/*").hasRole("boss")
                .antMatchers("/user/registration").permitAll()
                .antMatchers("/events*").hasAnyRole("boss", "dev", "USER")
                .antMatchers("/").permitAll().and().httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService());
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder().username("emma").password("emmaisboss").roles("boss").build());
//        return manager;
//    }
}
