package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.ChannelDto;
import de.mwa.evopiaserver.db.kotlin.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    private final ChannelRepository databaseWrapper;

    public ChannelService(ChannelRepository databaseWrapper) {
        this.databaseWrapper = databaseWrapper;
    }

    public List<ChannelDto> findAll() {
        var channels = databaseWrapper.findAllChannels();
        return channels.stream().map(chan -> new ChannelDto(chan.getName())).collect(Collectors.toList());
    }

    public int add(ChannelDto channelDto) {
        return databaseWrapper.saveChannel(channelDto);
    }

    public int remove(ChannelDto channelDto) {
        return databaseWrapper.deleteChannelByName(channelDto.getName());
    }
}
