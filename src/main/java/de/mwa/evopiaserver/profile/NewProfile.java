package de.mwa.evopiaserver.profile;


import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NewProfile {
    @NotBlank(message = "Email is mandatory.")
    private String email;
    @NotBlank(message = "Image is mandatory.")
    private String image;
    @NotBlank(message = "Tags are mandatory.")
    private List<Tag> tags;
    @NotBlank(message = "Channels are mandatory.")
    private String channels;
}
