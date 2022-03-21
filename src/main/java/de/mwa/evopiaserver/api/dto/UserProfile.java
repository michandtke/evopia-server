package de.mwa.evopiaserver.api.dto;

import java.util.List;
import java.util.Objects;

public class UserProfile {
    private String imagePath;
    private List<UserTag> tags;
    private List<UserChannel> profileChannels;

    public UserProfile(String imagePath, List<UserTag> tags, List<UserChannel> profileChannels) {
        this.imagePath = imagePath;
        this.tags = tags;
        this.profileChannels = profileChannels;
    }

    public UserProfile() {
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<UserTag> getTags() {
        return tags;
    }

    public void setTags(List<UserTag> tags) {
        this.tags = tags;
    }

    public List<UserChannel> getProfileChannels() {
        return profileChannels;
    }

    public void setProfileChannels(List<UserChannel> profileChannels) {
        this.profileChannels = profileChannels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(imagePath, that.imagePath) && Objects.equals(tags, that.tags) && Objects.equals(profileChannels, that.profileChannels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imagePath, tags, profileChannels);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "imagePath='" + imagePath + '\'' +
                ", tags=" + tags +
                ", profileChannels=" + profileChannels +
                '}';
    }
}
