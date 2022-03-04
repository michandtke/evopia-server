package de.mwa.evopiaserver.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.mwa.evopiaserver.registration.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = "user")
@Entity
public class Profile {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "profiletags",
            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;

    @OneToMany(mappedBy = "profileId")
    private List<ProfileChannel> profileChannels;

    public Profile() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id)
                && Objects.equals(user, profile.user)
                && Objects.equals(image, profile.image)
                && Objects.equals(tags, profile.tags)
                && Objects.equals(profileChannels, profile.profileChannels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, image, tags, profileChannels);
    }
}
