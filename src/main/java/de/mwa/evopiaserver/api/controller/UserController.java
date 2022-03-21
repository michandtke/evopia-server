package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.NoRemoteUserFoundException;
import de.mwa.evopiaserver.api.dto.UpsertUserDto;
import de.mwa.evopiaserver.api.dto.UserProfile;
import de.mwa.evopiaserver.db.kotlin.UserTagRepository;
import de.mwa.evopiaserver.registration.User;
import de.mwa.evopiaserver.service.UserChannelService;
import de.mwa.evopiaserver.service.UserServiceNew;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    private final UserServiceNew userService;
    private final UserChannelService userChannelService;
    private final UserTagRepository userTagRepository;

    public UserController(UserServiceNew userService, UserChannelService userChannelService, UserTagRepository userTagRepository) {
        this.userService = userService;
        this.userChannelService = userChannelService;
        this.userTagRepository = userTagRepository;
    }

    @GetMapping("/v2/user")
    public User getUser(final HttpServletRequest request) {
        if (request.getRemoteUser() == null)
            throw new NoRemoteUserFoundException("Too bad, no remote user found!");
        return userService.find(request.getRemoteUser());
    }

    @PutMapping("/v2/user")
    public User addNewUser(@RequestBody User newUser) {
        return userService.registerNewUserAccount(newUser);

    }

    @PostMapping("/v2/user")
    public String upsertUser(@RequestBody UpsertUserDto upsertUser, HttpServletRequest request) {
        if (request.getRemoteUser() == null)
            throw new NoRemoteUserFoundException("Too bad, no remote user found!");
        var updatedCount = userService.update(upsertUser, request.getRemoteUser());
        return "Updated " + updatedCount + " user.";
    }

    @GetMapping("/v2/user/profile")
    public UserProfile getUserProfile(final HttpServletRequest request) {
        if (request.getRemoteUser() == null)
            throw new NoRemoteUserFoundException("Too bad, no remote user found!");
        var user = userService.find(request.getRemoteUser());
        var path = user.getImagePath() == null ? "" : user.getImagePath();
        var channels = userChannelService.getChannels(request.getRemoteUser());
        var tags = userTagRepository.selectFor(request.getRemoteUser());
        return new UserProfile(path, tags, channels);
    }
}
