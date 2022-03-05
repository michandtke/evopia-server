package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.UserChannel;
import de.mwa.evopiaserver.api.dto.UserProfile;
import de.mwa.evopiaserver.api.dto.UserTag;
import de.mwa.evopiaserver.db.profile.Profile;
import de.mwa.evopiaserver.db.tag.Tag;
import de.mwa.evopiaserver.profile.ProfileChannel;
import de.mwa.evopiaserver.registration.User;
import de.mwa.evopiaserver.registration.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileService {
    private final UserRepository userRepository;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfile findUserProfile(String username) {
        User user = userRepository.findByEmail(username);
        Profile profile = user.getProfile();
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
}
