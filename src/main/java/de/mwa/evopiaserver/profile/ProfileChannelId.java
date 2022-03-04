package de.mwa.evopiaserver.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ProfileChannelId implements Serializable {

    private BigInteger profileId;
    private BigInteger channelId;

    public ProfileChannelId(BigInteger profileId, BigInteger channelId) {
        this.profileId = profileId;
        this.channelId = channelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileChannelId that = (ProfileChannelId) o;
        return Objects.equals(profileId, that.profileId) && Objects.equals(channelId, that.channelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, channelId);
    }
}