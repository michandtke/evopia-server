package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.UserChannel;
import de.mwa.evopiaserver.db.kotlin.UserChannelRepositoryNew;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserChannelService {
    private final UserChannelRepositoryNew userChannelRepository;

    public UserChannelService(UserChannelRepositoryNew userChannelRepository) {
        this.userChannelRepository = userChannelRepository;
    }

    public List<UserChannel> getChannels(String email) {
        return userChannelRepository.findForUser(email);
    }
}
