package de.mwa.evopiaserver.api.dto;

import java.util.Objects;

public class UserTag {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserTag() {
    }

    public UserTag(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTag userTag = (UserTag) o;
        return Objects.equals(name, userTag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "UserTag{" +
                "name='" + name + '\'' +
                '}';
    }
}
