package de.mwa.evopiaserver.profile;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NewProfile {
    private String email;
    @NotBlank(message = "Image is mandatory.")
    private String image;
    @NotNull(message = "Tags are mandatory.")
    private List<Tag> tags;
    @NotNull(message = "Channels with values are mandatory.")
    private List<NewProfileChannel> profileChannels;
}
