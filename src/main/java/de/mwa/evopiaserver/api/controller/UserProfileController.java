package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.dto.UserProfile;
import de.mwa.evopiaserver.service.UserProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/v2/profile")
    public UserProfile getProfile(final HttpServletRequest request) {
        if (request.getRemoteUser() != null)
            return userProfileService.findUserProfile(request.getRemoteUser());
        return null;
    }
}
