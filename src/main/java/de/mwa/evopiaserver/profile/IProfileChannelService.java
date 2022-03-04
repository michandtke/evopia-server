package de.mwa.evopiaserver.profile;

import java.util.List;
import java.util.Map;

public interface IProfileChannelService {
    List<ProfileChannel> upsert(Long profileId, Map<String, String> channelWithValue);
}
