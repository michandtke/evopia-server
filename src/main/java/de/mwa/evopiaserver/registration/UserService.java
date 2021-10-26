package de.mwa.evopiaserver.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistsException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with that email address: "
            + userDto.getEmail());
        }

        return null;
    }

    private boolean emailExists(String email) {
        return userRepository.findEmail(email) != null;
    }
}
