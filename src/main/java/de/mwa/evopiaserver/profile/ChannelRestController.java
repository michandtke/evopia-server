package de.mwa.evopiaserver.profile;

import de.mwa.evopiaserver.db.channel.Channel;
import de.mwa.evopiaserver.db.channel.ChannelRepository;
import de.mwa.evopiaserver.registration.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class ChannelRestController {

    @Autowired
    private ChannelRepository channelRepository;

    @PostMapping("/channel/register")
    public GenericResponse registerChannel(@Valid @RequestBody Channel channel,
                                           final HttpServletRequest request) {
        Channel saved = channelRepository.save(channel);
        return new GenericResponse("Saved channel " + saved);
    }
}
