package de.mwa.evopiaserver.profile;


import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NewProfile {
    @NotNull
    private String email;
    @NotNull
    private String image;
    @NotNull
    private String tags;
    @NotNull
    private String channels;
}
