package de.mwa.evopiaserver.api.dto;

import java.util.Objects;

public class UserChannel {
    private String name;
    private String value;

    public UserChannel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public UserChannel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserChannel that = (UserChannel) o;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "UserChannel{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
