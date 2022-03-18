package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.UpsertUserDto;
import de.mwa.evopiaserver.db.kotlin.UserRepositoryNew;
import de.mwa.evopiaserver.registration.User;
import de.mwa.evopiaserver.registration.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceNew {
    private final UserRepositoryNew userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceNew(UserRepositoryNew userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUserAccount(User user) throws UserAlreadyExistsException {
        if (userRepository.emailAlreadyExists(user.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with that email address: "
                    + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return user;
    }

    public User find(String mail) {
        return userRepository.findByEmail(mail);
    }

    public int update(UpsertUserDto upsertUser, String mail) {
        return userRepository.update(mail, upsertUser);
    }
}
