package de.mwa.evopiaserver.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileChannelService implements IProfileChannelService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final ProfileChannelRepository profileChannelRepository;
    private final ChannelRepository channelRepository;

    public ProfileChannelService(ProfileChannelRepository profileChannelRepository, ChannelRepository channelRepository) {
        this.profileChannelRepository = profileChannelRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public List<ProfileChannel> upsert(Long profileId, Map<String, String> channelWithValue) {
        List<ProfileChannel> profileChannelUpserts = knownChannelIdsWithValues(channelWithValue).stream()
                .map(chanWithValue -> new ProfileChannel(profileId, chanWithValue.getKey(), chanWithValue.getValue()))
                .collect(Collectors.toList());

        return profileChannelRepository.saveAll(profileChannelUpserts);
    }

    List<Map.Entry<Long, String>> knownChannelIdsWithValues(Map<String, String> channelWithValue) {
        Set<String> neededChannelNames = channelWithValue.keySet();
        List<Channel> channels = channelRepository.findAll();
        List<String> channelNames = channels.stream().map(Channel::getName).collect(Collectors.toList());

        List<String> unknownChannels = neededChannelNames.stream()
                .filter(name -> !channelNames.contains(name)).collect(Collectors.toList());
        LOGGER.warn("unknown channels: " + String.join(",", unknownChannels));

        return channelWithValue.entrySet().stream()
                .map(chanWithValue -> getChannelIdIfExists(channels, chanWithValue))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<Map.Entry<Long, String>> getChannelIdIfExists(List<Channel> channels, Map.Entry<String, String> chanWithValue) {
        return channels.stream()
                .filter(chan -> chan.getName().equals(chanWithValue.getKey()))
                .map(chan -> Map.entry(chan.getId(), chanWithValue.getValue()))
                .findFirst();
    }
}
