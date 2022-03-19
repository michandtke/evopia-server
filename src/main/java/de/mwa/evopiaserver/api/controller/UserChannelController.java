package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.UnknownChannelException;
import de.mwa.evopiaserver.api.dto.ChannelDto;
import de.mwa.evopiaserver.api.dto.UserChannel;
import de.mwa.evopiaserver.service.ChannelService;
import de.mwa.evopiaserver.service.UserChannelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserChannelController {
    private final UserChannelService userChannelService;
    private final ChannelService channelService;

    public UserChannelController(UserChannelService userChannelService,
                                 ChannelService channelService) {
        this.userChannelService = userChannelService;
        this.channelService = channelService;
    }

    @GetMapping("/v2/user/channel")
    public List<UserChannel> getUserChannels(HttpServletRequest request) {
        return userChannelService.getChannels(request.getRemoteUser());
    }

    @PostMapping("/v2/user/channel")
    public String replaceUserChannels(HttpServletRequest request, @RequestBody List<UserChannel> channels) {
//        return userChannelService.replaceWith(request.getRemoteUser(), channels);
        var allChannels = channelService.findAll().stream().map(ChannelDto::getName).collect(Collectors.toList());
        var unknownChannels = channels.stream().filter(chan -> !allChannels.contains(chan.getName())).collect(Collectors.toList());
        if (!unknownChannels.isEmpty()) {
            var names = unknownChannels.stream().map(UserChannel::getName).collect(Collectors.toList());
            var message = "Unknown channel: " + String.join(",", names);
            throw new UnknownChannelException(message);
        }
        var added = userChannelService.add(request.getRemoteUser(), channels);

        return "Successfully replaced channels. Added : " + added.getFirst() + " | Deleted: " + added.getSecond();
    }
}
