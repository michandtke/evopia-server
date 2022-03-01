package de.mwa.evopiaserver.profile;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NewProfile {
    @NotBlank
    private String email;
    @NotBlank
    private String image;
    @NotBlank
    private String tags;
    @NotBlank
    private String channels;
}
