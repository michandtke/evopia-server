package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.dto.UserTag;
import de.mwa.evopiaserver.db.kotlin.UserTagRepository;
import kotlin.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserTagController {

    private final UserTagRepository userTagRepository;

    public UserTagController(UserTagRepository userTagRepository) {
        this.userTagRepository = userTagRepository;
    }

    @GetMapping("/v2/user/tags")
    public List<UserTag> getUserTags(HttpServletRequest request) {
        return userTagRepository.selectFor(request.getRemoteUser());
    }

    @PostMapping("/v2/user/tags")
    public String upsertUserTags(@RequestBody List<UserTag> newUserTags, HttpServletRequest request) {
        Pair<Integer, Integer> upsertedDeleted = userTagRepository.upsert(request.getRemoteUser(), newUserTags);
        return "Upserted user tags: " + upsertedDeleted.getFirst() + " | Deleted user tags: " + upsertedDeleted.getSecond();
    }
}
