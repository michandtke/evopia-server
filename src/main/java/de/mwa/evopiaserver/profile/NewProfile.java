package de.mwa.evopiaserver.profile;


import lombok.*;

import javax.validation.constraints.NotBlank;

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
    private String tags;
    @NotBlank(message = "Channels are mandatory.")
    private String channels;
}
