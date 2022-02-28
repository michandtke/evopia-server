package de.mwa.evopiaserver.profile;

import de.mwa.evopiaserver.registration.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class ProfileController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/profile")
    public GenericResponse registerProfile(@RequestBody @Valid Profile newProfile,
                                           final HttpServletRequest request) {
        LOGGER.info("Trying to register a new profile: " + newProfile.toString());
        Profile saved = profileRepository.save(newProfile);
        return new GenericResponse("success as id : " + saved.getId());
    }
}
