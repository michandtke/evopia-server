package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.db.kotlin.UserRepositoryNew;
import de.mwa.evopiaserver.registration.User;
import de.mwa.evopiaserver.registration.UserAlreadyExistsException;
import de.mwa.evopiaserver.registration.UserDto;
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

    public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistsException {
        if (userRepository.emailAlreadyExists(userDto.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        userRepository.save(user);

        return user;
    }

    public User find(String mail) {
        return userRepository.findByEmail(mail);
    }
}
