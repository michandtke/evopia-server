package de.mwa.evopiaserver.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
   @Id
   private BigInteger channelId;

   private String value;

   ProfileChannel(Long profileId, Long channelId, String value) {
      this.profileId = BigInteger.valueOf(profileId);
      this.channelId = BigInteger.valueOf(channelId);
      this.value = value;
   }
}
