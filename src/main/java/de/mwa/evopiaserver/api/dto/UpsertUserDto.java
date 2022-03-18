package de.mwa.evopiaserver.api.dto;

import java.util.Objects;

public class UpsertUserDto {
    private String imagePath;

    public UpsertUserDto(String imagePath) {
        this.imagePath = imagePath;
    }

    public UpsertUserDto() {}

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpsertUserDto that = (UpsertUserDto) o;
        return Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imagePath);
    }
}
