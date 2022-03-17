package de.mwa.evopiaserver.profile;

import java.util.List;

public interface IProfileChannelService {
    int upsert(Long profileId, List<NewProfileChannel> profileChannels);
}
