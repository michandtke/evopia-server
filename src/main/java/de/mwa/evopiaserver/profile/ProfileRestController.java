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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProfileRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    private final TagRepository tagRepository;
    private final ChannelRepository channelRepository;

    public ProfileRestController(ProfileRepository profileRepository, UserRepository userRepository, TagRepository tagRepository, ChannelRepository channelRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.channelRepository = channelRepository;
    }

    @PostMapping("/register/profile")
    public GenericResponse registerProfile(@Valid @RequestBody NewProfile newProfile,
                                           final HttpServletRequest request) {
        LOGGER.info("Trying to register a new profile: " + newProfile.toString());
        var currentUser = getEmail(request);
        LOGGER.info("Current user: " + currentUser);
        var newChannelNames = newProfile.getChannels().stream().map(Channel::getName).collect(Collectors.toList());
        var newTagNames = newProfile.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        var user = userRepository.findByEmail(currentUser);
        Profile profile = new Profile();
        if (user.getProfile() != null) {
            var oldId = user.getProfile().getId();
            profile.setId(oldId);
            LOGGER.info("Upserting a new profile. Old id: " + oldId);
        } else {
            LOGGER.info("Old profile not found for email address, register a new one.");
        }
        profile.setUser(user);
        List<Channel> channels = channelRepository.findAll().stream()
                .filter(chan -> newChannelNames.contains(chan.getName()))
                .collect(Collectors.toList());
        List<Tag> tags = tagRepository.findAll().stream()
                .filter(tag -> newTagNames.contains(tag.getName()))
                .collect(Collectors.toList());
        profile.setTags(tags);
        profile.setChannels(channels);
        profile.setImage(newProfile.getImage());
        Profile saved = profileRepository.save(profile);
        if (saved == null)
            return new GenericResponse("Unfortunately, an error.", "Not saved - it is null!");
//        return new GenericResponse("success as id : " + saved.getId());
        return new GenericResponse("success: " + saved);
    }

    @GetMapping("/profile")
    public Profile getProfile(final HttpServletRequest request) {
        if (request.getRemoteUser() != null)
            return userRepository.findByEmail(getEmail(request)).getProfile();
        return null;
    }

    private String getEmail(final HttpServletRequest request) {
        return request.getRemoteUser();
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
