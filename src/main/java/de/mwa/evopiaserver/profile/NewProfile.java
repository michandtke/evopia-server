package de.mwa.evopiaserver.profile;


import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NewProfile {

    private String email;
    private String image;
    private String tags;
    private String channels;
}
