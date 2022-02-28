package de.mwa.evopiaserver.profile;

import de.mwa.evopiaserver.registration.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class Profile {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne
    @JoinColumn(name = "id")
    private User user;
    private String image;
    private String tags;
    private String channels;

    public Profile() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id)
                && Objects.equals(image, profile.image)
                && Objects.equals(tags, profile.tags)
                && Objects.equals(channels, profile.channels)
                && Objects.equals(user, profile.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image, tags, channels, user);
    }
}
