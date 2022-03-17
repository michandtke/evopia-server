package de.mwa.evopiaserver.profile;

import de.mwa.evopiaserver.db.channel.Channel;
import de.mwa.evopiaserver.db.kotlin.ChannelRepository;
import de.mwa.evopiaserver.db.kotlin.ProfileChannelRepositoryNew;
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

    private final ProfileChannelRepositoryNew profileChannelRepository;
    private final ChannelRepository databaseWrapper;

    public ProfileChannelService(ProfileChannelRepositoryNew profileChannelRepository,
                                 ChannelRepository channelRepository) {
        this.profileChannelRepository = profileChannelRepository;
        this.databaseWrapper = channelRepository;
    }

    @Override
    public int upsert(Long profileId, List<NewProfileChannel> profileChannels) {
        List<ProfileChannel> profileChannelUpserts = knownChannelsWithValues(profileChannels).stream()
                .map(chanWithValue -> new ProfileChannel(profileId.intValue(), chanWithValue.getKey().getId().intValue(), chanWithValue.getValue()))
                .collect(Collectors.toList());
        var profileChannelString = profileChannelUpserts.stream().map(ProfileChannel::toString).collect(Collectors.toSet());
        LOGGER.info("SaveAll: " + String.join(", ", profileChannelString));
        return profileChannelRepository.saveAll(profileChannelUpserts);
    }

    List<Map.Entry<Channel, String>> knownChannelsWithValues(List<NewProfileChannel> profileChannels) {
        Set<String> neededChannelNames = profileChannels.stream().map(NewProfileChannel::getName).collect(Collectors.toSet());
        var channels = databaseWrapper.findAllChannels();
        List<String> channelNames = channels.stream().map(Channel::getName).collect(Collectors.toList());

        List<String> unknownChannels = neededChannelNames.stream()
                .filter(name -> !channelNames.contains(name)).collect(Collectors.toList());
        LOGGER.warn("unknown channels: " + String.join(",", unknownChannels));

        return profileChannels.stream()
                .map(chanWithValue -> getChannelIfExists(channels, chanWithValue))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<Map.Entry<Channel, String>> getChannelIfExists(List<Channel> channels, NewProfileChannel newProfileChannel) {
        return channels.stream()
                .filter(chan -> chan.getName().equals(newProfileChannel.getName()))
                .map(chan -> Map.entry(chan, newProfileChannel.getValue()))
                .findFirst();
    }
}
