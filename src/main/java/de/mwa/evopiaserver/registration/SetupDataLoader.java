package de.mwa.evopiaserver.registration;


import de.mwa.evopiaserver.db.profile.Profile;
import de.mwa.evopiaserver.db.profile.ProfileRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository, ProfileRepository profileRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial roles
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        final Role userRole = createRoleIfNotFound("ROLE_USER");

        // == create initial user
        createUserIfNotFound("test@test.com", "Test", "Test", "test", new ArrayList<>(Arrays.asList(adminRole, userRole)));

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(final String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    User createUserIfNotFound(final String email, final String firstName, final String lastName, final String password, final Collection<Role> roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            String encode = passwordEncoder.encode(password);
            user.setPassword(encode);
            user.setEmail(email);
//            user.setEnabled(true);
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        var profile = createProfileForUser(user);
        return user;
    }

    Profile createProfileForUser(User forUser) {
        var profile = new Profile();
        profile.setUser(forUser);
        profile.setImage("");
        profile.setTags(List.of());
        profile.setProfileChannels(List.of());
        return profileRepository.save(profile);
    }

}
