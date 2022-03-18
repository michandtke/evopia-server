package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.dto.UserChannel;
import de.mwa.evopiaserver.service.UserChannelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserChannelController {
    private final UserChannelService userChannelService;

    public UserChannelController(UserChannelService userChannelService) {
        this.userChannelService = userChannelService;
    }

    @GetMapping("/v2/user/channel")
    public List<UserChannel> getUserChannels(HttpServletRequest request) {
        return userChannelService.getChannels(request.getRemoteUser());
    }

    @PostMapping("/v2/user/channel")
    public int replaceUserChannels(HttpServletRequest request, @RequestBody List<UserChannel> channels) {
//        return userChannelService.replaceWith(request.getRemoteUser(), channels);
        return userChannelService.add(request.getRemoteUser(), channels);
    }
}
