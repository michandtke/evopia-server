package de.mwa.evopiaserver.service;

import de.mwa.evopiaserver.api.dto.ChannelDto;
import de.mwa.evopiaserver.db.kotlin.DatabaseWrapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ChannelService {

    private final DatabaseWrapper databaseWrapper;

    public ChannelService(DatabaseWrapper databaseWrapper) {
        this.databaseWrapper = databaseWrapper;
    }

    public List<ChannelDto> findAll() {
        var channels = databaseWrapper.findAllChannels();
        return channels;
    }

//    @Transactional
    public int add(ChannelDto channelDto) {
        return databaseWrapper.saveChannel(channelDto);
    }

//    @Transactional
    public int remove(ChannelDto channelDto) {
        return databaseWrapper.deleteChannelByName(channelDto.getName());
    }
}
