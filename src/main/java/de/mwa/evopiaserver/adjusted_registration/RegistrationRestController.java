package de.mwa.evopiaserver.adjusted_registration;

import de.mwa.evopiaserver.registration.IUserService;
import de.mwa.evopiaserver.registration.User;
import de.mwa.evopiaserver.registration.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class RegistrationRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;

    @PostMapping("/user/registration")
    public GenericResponse registerUserAccount(@Valid UserDto accountDto, final HttpServletRequest request) {
        LOGGER.info("Registering user account with information: {}", accountDto);

        final User registered = userService.registerNewUserAccount(accountDto);
        return new GenericResponse("success");
    }

}
