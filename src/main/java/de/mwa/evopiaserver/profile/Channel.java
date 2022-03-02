package de.mwa.evopiaserver.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = "profiles")
@Entity
public class Channel {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String name;

    @ManyToMany(mappedBy = "channels")
    @JsonIgnore
    private Collection<Profile> profiles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id)
                && Objects.equals(name, channel.name)
                && Objects.equals(profiles, channel.profiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profiles);
    }
}
