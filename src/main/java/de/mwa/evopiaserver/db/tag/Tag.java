package de.mwa.evopiaserver.db.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.mwa.evopiaserver.db.profile.Profile;
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
public class Tag {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Collection<Profile> profiles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id)
                && Objects.equals(name, tag.name)
                && Objects.equals(profiles, tag.profiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profiles);
    }
}
