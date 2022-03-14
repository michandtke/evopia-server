package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.UserChannel;
import de.mwa.evopiaserver.api.dto.UserProfile;
import de.mwa.evopiaserver.api.dto.UserTag;
import de.mwa.evopiaserver.db.channel.Channel;
import de.mwa.evopiaserver.db.channel.ChannelRepository;
import de.mwa.evopiaserver.db.profile.Profile;
import de.mwa.evopiaserver.db.profile.ProfileRepository;
import de.mwa.evopiaserver.db.tag.Tag;
import de.mwa.evopiaserver.db.tag.TagRepository;
import de.mwa.evopiaserver.profile.ProfileChannel;
import de.mwa.evopiaserver.profile.ProfileChannelRepository;
import de.mwa.evopiaserver.registration.User;
import de.mwa.evopiaserver.registration.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final TagRepository tagRepository;
    private final ChannelRepository channelRepository;
    private final ProfileChannelRepository profileChannelRepository;

    public UserProfileService(UserRepository userRepository, ProfileRepository profileRepository, TagRepository tagRepository, ChannelRepository channelRepository, ProfileChannelRepository profileChannelRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.tagRepository = tagRepository;
        this.channelRepository = channelRepository;
        this.profileChannelRepository = profileChannelRepository;
    }

    public UserProfile findUserProfile(String username) {
        var profile = getOrCreateProfile(username);
        return asDto(profile);
    }

    private Profile getOrCreateProfile(String username) {
        User user = userRepository.findByEmail(username);
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = buildNewProfile(user);
            profileRepository.save(profile);
        }
        return profile;
    }

    private Profile buildNewProfile(User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setTags(List.of());
        profile.setProfileChannels(List.of());
        profile.setImage("");
        return profile;
    }

    private UserProfile asDto(Profile profile) {
        return UserProfile.builder()
                .imagePath(profile.getImage())
                .profileChannels(mapProfileChannels(profile))
                .tags(mapProfileTags(profile))
                .build();
    }

    private List<UserChannel> mapProfileChannels(Profile profile) {
        return profile.getProfileChannels().stream()
                .map(this::mapProfileChannel)
                .collect(Collectors.toList());
    }

    private UserChannel mapProfileChannel(ProfileChannel profileChannel) {
        return UserChannel.builder()
                .name(profileChannel.getChannel().getName())
                .value(profileChannel.getValue())
                .build();
    }

    private List<UserTag> mapProfileTags(Profile profile) {
        return profile.getTags().stream()
                .map(this::mapProfileTag)
                .collect(Collectors.toList());
    }

    private UserTag mapProfileTag(Tag tag) {
        return UserTag.builder()
                .name(tag.getName())
                .build();
    }

    @Transactional
    public UserProfile saveUserProfile(String username, UserProfile profileToSave) {
        Profile profile = getOrCreateProfile(username);

        profile.setImage(profileToSave.getImagePath());

        var userTags = profileToSave.getTags();
        if (userTags != null) {
            Set<String> tagNames = userTags.stream().map(UserTag::getName).collect(Collectors.toSet());
            var tags = tagRepository.findByNameIn(tagNames);
            profile.setTags(new ArrayList<>(tags));
        }

        var userChannels = profileToSave.getProfileChannels();
        if (userChannels != null) {
            var channelNames = userChannels.stream().map(UserChannel::getName).collect(Collectors.toList());
            var channels = channelRepository.findByNameIn(channelNames);
            List<ProfileChannel> channelDaos = userChannels.stream()
                    .map(chan -> buildProfileChannel(profile.getId(), chan.getValue(), findChannel(channels, chan.getName())))
                    .collect(Collectors.toList());
            profileChannelRepository.saveAll(channelDaos);
            profile.setProfileChannels(channelDaos);
        }
        var dao = profileRepository.save(profile);

        return asDto(dao);
    }

    private Channel findChannel(Collection<Channel> channels, String name) {
        return channels.stream().filter(chan -> chan.getName().equals(name)).findFirst().get();
    }

    private ProfileChannel buildProfileChannel(long profileId, String value, Channel channel) {
        ProfileChannel newChannel = new ProfileChannel();
        newChannel.setProfileId(BigInteger.valueOf(profileId));
        newChannel.setValue(value);
        newChannel.setChannel(channel);
        return newChannel;
    }
}
