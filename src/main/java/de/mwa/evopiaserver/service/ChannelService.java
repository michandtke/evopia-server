package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.ChannelDto;
import de.mwa.evopiaserver.db.channel.Channel;
import de.mwa.evopiaserver.db.channel.ChannelRepository;
import de.mwa.evopiaserver.db.kotlin.DatabaseWrapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ChannelService {

    private final DatabaseWrapper databaseWrapper;
    private final ChannelRepository channelRepository;

    public ChannelService(DatabaseWrapper databaseWrapper,
                          ChannelRepository channelRepository) {
        this.databaseWrapper = databaseWrapper;
        this.channelRepository = channelRepository;
    }

    public List<ChannelDto> findAll() {
        var database = databaseWrapper.connect();

        var channels = databaseWrapper.findAllChannels(database);
        return channels;
    }

    private ChannelDto asDto(Channel channel) {
        return ChannelDto.builder()
                .name(channel.getName())
                .build();
    }

    @Transactional
    public ChannelDto add(ChannelDto channelDto) {
        Channel channel = asDao(channelDto);
        var saved = channelRepository.save(channel);
        return asDto(saved);
    }

    private Channel asDao(ChannelDto channelDto) {
        var channel = new Channel();
        channel.setName(channelDto.getName());
        return channel;
    }

    @Transactional
    public ChannelDto remove(ChannelDto channelDto) {
        channelRepository.deleteByName(channelDto.getName());
        return channelDto;
    }
}
