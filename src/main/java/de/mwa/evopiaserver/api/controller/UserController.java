package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.NoRemoteUserFoundException;
import de.mwa.evopiaserver.api.dto.UpsertUserDto;
import de.mwa.evopiaserver.registration.User;
import de.mwa.evopiaserver.service.UserServiceNew;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    private final UserServiceNew userService;

    public UserController(UserServiceNew userService) {
        this.userService = userService;
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


//    private final UserProfileService userProfileService;
//
//    public UserProfileController(UserProfileService userProfileService) {
//        this.userProfileService = userProfileService;
//    }
//
//    @GetMapping("/v2/profile")
//    public UserProfile getProfile(final HttpServletRequest request) {
//        if (request.getRemoteUser() == null)
//            throw new NoRemoteUserFoundException("Too bad, no remote user found!");
//        return userProfileService.findUserProfile(request.getRemoteUser());
//    }
//
//    @PostMapping("/v2/profile/save")
//    public UserProfile saveUserProfile(final HttpServletRequest request,
//                                       @RequestBody UserProfile profileToSave) {
//        if (request.getRemoteUser() == null)
//            throw new NoRemoteUserFoundException("Too bad, no remote user found!");
//        return userProfileService.saveUserProfile(request.getRemoteUser(), profileToSave);
//    }
}
