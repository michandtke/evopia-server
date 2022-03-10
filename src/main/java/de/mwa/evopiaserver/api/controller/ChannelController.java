package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.dto.ChannelDto;
import de.mwa.evopiaserver.service.ChannelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @GetMapping("/v2/channels")
    List<ChannelDto> channels() {
        return channelService.findAll();
    }

    @PostMapping("/v2/channels/add")
    String addChannel(@RequestBody ChannelDto channelToAdd) {
        return "Added " + channelService.add(channelToAdd);
    }

    @PostMapping("/v2/channels/remove")
    String removeChannel(@RequestBody ChannelDto channelToAdd) {
        return "Removed " + channelService.remove(channelToAdd);
    }
}
