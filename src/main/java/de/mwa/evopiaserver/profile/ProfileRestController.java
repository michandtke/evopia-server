package de.mwa.evopiaserver.profile;

import de.mwa.evopiaserver.registration.GenericResponse;
import de.mwa.evopiaserver.registration.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProfileRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    public ProfileRestController(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/register/profile")
    public GenericResponse registerProfile(@Valid @RequestBody NewProfile newProfile,
                                           final HttpServletRequest request) {
        LOGGER.info("Trying to register a new profile: " + newProfile.toString());
        var user = userRepository.findByEmail(newProfile.getEmail());
        Profile profile = new Profile();
        profile.setChannels(newProfile.getChannels());
        profile.setUser(user);
        profile.setTags(newProfile.getTags());
        profile.setImage(newProfile.getImage());
        Profile saved = profileRepository.save(profile);
        if (saved == null)
            return new GenericResponse("Unfortunately, an error.", "Not saved - it is null!");
        return new GenericResponse("success as id : " + saved.getId());
    }

    @GetMapping("/profile")
    public Profile getProfile(@PathVariable String email) {
        return userRepository.findByEmail(email).getProfile();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
