package de.mwa.evopiaserver;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class Event {
    private @Id @GeneratedValue Long id;
    private String name;
    private String description;
    private String date;
    private String time;
    private String place;
    private String tags;

    public Event() {}

    public Event(String name, String description, String date, String time, String place, String tags) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.place = place;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(name, event.name) &&
                Objects.equals(description, event.description) &&
                Objects.equals(date, event.date) &&
                Objects.equals(time, event.time) &&
                Objects.equals(place, event.place) &&
                Objects.equals(tags, event.tags);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
