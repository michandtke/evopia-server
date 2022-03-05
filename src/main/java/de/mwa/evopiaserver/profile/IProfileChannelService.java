package de.mwa.evopiaserver.profile;

import java.util.List;

public interface IProfileChannelService {
    List<ProfileChannel> upsert(Long profileId, List<NewProfileChannel> profileChannels);
}
