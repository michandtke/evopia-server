package de.mwa.evopiaserver.profile;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@NoArgsConstructor
public class ProfileChannel {

   private int userId;
   private int channelId;
   private String value;

   ProfileChannel(int userId, int channelId, String value) {
      this.userId = userId;
      this.channelId = channelId;
      this.value = value;
   }

   public int getChannelId() {
      return channelId;
   }

   public int getUserId() {
      return userId;
   }

   public String getValue() {
      return value;
   }
}
