package de.mwa.evopiaserver.registration;

import de.mwa.evopiaserver.db.kotlin.UserRepositoryNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepositoryNew userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistsException {
        if (userRepository.emailAlreadyExists(userDto.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with that email address: "
            + userDto.getEmail());
        }

        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        userRepository.save(user);

        return user;
    }
}
