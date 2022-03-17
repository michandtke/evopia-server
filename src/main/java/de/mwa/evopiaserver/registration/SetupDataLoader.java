package de.mwa.evopiaserver.registration;


import de.mwa.evopiaserver.db.kotlin.UserRepositoryNew;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepositoryNew userRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepositoryNew userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial user
        createUserIfNotFound("test@test.com", "Test", "Test", "test");

        alreadySetup = true;
    }

    @Transactional
    User createUserIfNotFound(final String email, final String firstName, final String lastName, final String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            String encode = passwordEncoder.encode(password);
            user.setPassword(encode);
            user.setEmail(email);
            user.setImagePath("");
        }
        userRepository.save(user);
        return user;
    }

}
