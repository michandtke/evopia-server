package de.mwa.evopiaserver.profile;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;

@Entity(name = "profilechannels")
@Getter
@Setter
@IdClass(ProfileChannelId.class)
@ToString
@NoArgsConstructor
public class ProfileChannel {
   @Id
   private BigInteger profileId;

   @ManyToOne
   @Id
   @JsonUnwrapped
   private Channel channel;

   private String value;

   ProfileChannel(Long profileId, Channel channel, String value) {
      this.profileId = BigInteger.valueOf(profileId);
      this.channel = channel;
      this.value = value;
   }
}
