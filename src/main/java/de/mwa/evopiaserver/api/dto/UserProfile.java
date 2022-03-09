package de.mwa.evopiaserver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String imagePath;
    private List<UserTag> tags;
    private List<UserChannel> profileChannels;
}
