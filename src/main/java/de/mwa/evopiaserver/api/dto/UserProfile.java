package de.mwa.evopiaserver.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfile {
    String imagePath;
    final List<UserTag> tags;
    final List<UserChannel> profileChannels;
}
