package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.ChannelDto;
import de.mwa.evopiaserver.db.channel.Channel;
import de.mwa.evopiaserver.db.channel.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public List<ChannelDto> findAll() {
        List<Channel> channels = channelRepository.findAll();
        return mapTo(channels, this::asDto);
    }

    private <T, R> List<R> mapTo(List<T> input, Function<T, R> mapper) {
        return input.stream().map(mapper).collect(Collectors.toList());
    }

    private ChannelDto asDto(Channel channel) {
        return ChannelDto.builder()
                .name(channel.getName())
                .build();
    }

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
}
