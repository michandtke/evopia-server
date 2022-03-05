package de.mwa.evopiaserver.profile;

import de.mwa.evopiaserver.db.channel.Channel;
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
    private Channel channel;

    public ProfileChannelId(BigInteger profileId, Channel channel) {
        this.profileId = profileId;
        this.channel = channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileChannelId that = (ProfileChannelId) o;
        return Objects.equals(profileId, that.profileId) && Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, channel);
    }
}