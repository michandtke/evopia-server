package de.mwa.evopiaserver.service

import de.mwa.evopiaserver.api.dto.ChannelDto
import de.mwa.evopiaserver.db.kotlin.ChannelRepository
import java.util.stream.Collectors

class ChannelService(private val databaseWrapper: ChannelRepository) {
    fun findAll(): List<ChannelDto> {
        val channels = databaseWrapper.findAllChannels()
        return channels.stream().map { (name): ChannelDto -> ChannelDto(name) }.collect(Collectors.toList())
    }

    fun add(channelDto: ChannelDto?): Int {
        return databaseWrapper.saveChannel(channelDto!!)
    }

    fun remove(channelDto: ChannelDto): Int {
        return databaseWrapper.deleteChannelByName(channelDto.name)
    }

    fun unknownChannelNames(channelNames: List<String>): List<String> {
        val allChannels = findAll().map { c -> c.name }
        return channelNames.filter { chan -> !allChannels.contains(chan) }
    }
}