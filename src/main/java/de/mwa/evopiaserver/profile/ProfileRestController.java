package de.mwa.evopiaserver.profile;

import de.mwa.evopiaserver.registration.GenericResponse;
import de.mwa.evopiaserver.registration.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class ProfileRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/profile")
    public GenericResponse registerProfile(@RequestBody @Valid NewProfile newProfile,
                                           final HttpServletRequest request) {
        LOGGER.info("Trying to register a new profile: " + newProfile.toString());
        var user = userRepository.findByEmail(newProfile.getEmail());
        Profile profile = new Profile();
        profile.setChannels(newProfile.getChannels());
        profile.setUser(user);
        profile.setTags(newProfile.getTags());
        profile.setImage(newProfile.getImage());
        Profile saved = profileRepository.save(profile);
        return new GenericResponse("success as id : " + saved.getId());
    }
}
