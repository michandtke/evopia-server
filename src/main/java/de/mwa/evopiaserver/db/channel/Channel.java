package de.mwa.evopiaserver.db.channel;

import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@ToString
public class Channel {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String name;

    public String getName() { return name; }

    public Long getId() { return id; }

    public void setName(String name) { this.name = name; }

    public void setId(Long id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id)
                && Objects.equals(name, channel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
