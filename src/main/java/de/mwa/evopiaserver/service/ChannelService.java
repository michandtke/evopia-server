package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.ChannelDto;
import de.mwa.evopiaserver.db.channel.Channel;
import de.mwa.evopiaserver.db.channel.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public List<ChannelDto> findAll() {
        List<Channel> channels = channelRepository.findAll();
        List<ChannelDto> asDtos = channels.stream().map(this::asDto).collect(Collectors.toList());
        return asDtos;
    }

    private ChannelDto asDto(Channel channel) {
        return ChannelDto.builder()
                .name(channel.getName())
                .build();
    }
}
