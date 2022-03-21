package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.dto.UserTag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserTagController {

    @GetMapping("/v2/user/tags")
    public List<UserTag> getUserTags() {
        return List.of();
    }
}
