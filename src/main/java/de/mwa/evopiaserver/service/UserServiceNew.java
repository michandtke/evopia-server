package de.mwa.evopiaserver.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.mwa.evopiaserver.api.dto.UpsertUserDto;
import de.mwa.evopiaserver.db.kotlin.UserRepositoryNew;
import de.mwa.evopiaserver.registration.User;
import de.mwa.evopiaserver.registration.UserAlreadyExistsException;

public class UserServiceNew {
    private final UserRepositoryNew userRepository;

    public UserServiceNew(UserRepositoryNew userRepository) {
        this.userRepository = userRepository;
    }

    public User registerNewUserAccount(User user) throws UserAlreadyExistsException {
        if (userRepository.emailAlreadyExists(user.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with that email address: "
                    + user.getEmail());
        }
        var encrypted = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(encrypted);

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
