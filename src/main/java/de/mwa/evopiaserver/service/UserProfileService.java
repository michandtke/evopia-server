package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.UserChannel;
import de.mwa.evopiaserver.api.dto.UserProfile;
import de.mwa.evopiaserver.api.dto.UserTag;
import de.mwa.evopiaserver.db.profile.Profile;
import de.mwa.evopiaserver.db.profile.ProfileRepository;
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
    private final ProfileRepository profileRepository;

    public UserProfileService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public UserProfile findUserProfile(String username) {
        User user = userRepository.findByEmail(username);
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = buildAndSaveNewProfile(user);
        }

        return UserProfile.builder()
                .imagePath(profile.getImage())
                .profileChannels(mapProfileChannels(profile))
                .tags(mapProfileTags(profile))
                .build();
    }

    private Profile buildAndSaveNewProfile(User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setTags(List.of());
        profile.setProfileChannels(List.of());
        profile.setImage("");
        return profileRepository.save(profile);
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
