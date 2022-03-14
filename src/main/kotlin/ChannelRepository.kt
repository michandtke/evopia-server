package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.ChannelDto
import de.mwa.evopiaserver.db.channel.Channel
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

@Component
class ChannelRepository(val databaseUtil: DatabaseUtil) {

    fun findAllChannels(): List<Channel> {
        val entries = databaseUtil.database.from(ChannelTable).select()
        return entries.map {
            rowToChannel(it)
        }
    }

    fun saveChannel(channel: ChannelDto): Int {
        return databaseUtil.database.insert(ChannelTable) {
            set(it.name, channel.name)
        }
    }

    fun deleteChannelByName(channelNameToDelete: String): Int {
        return databaseUtil.database.delete(ChannelTable) {
            it.name eq channelNameToDelete
        }
    }

    fun findChannelsByNameIn(channelNames: MutableList<String>): List<Channel> {
        return databaseUtil.database
                .from(ChannelTable)
                .select()
                .where { (ChannelTable.name inList channelNames) }
                .map { rowToChannel(it) }
    }

    private fun rowToChannel(it: QueryRowSet): Channel {
        val channel = Channel()
        channel.id = it[ChannelTable.id]?.toLong()
        channel.name = it[ChannelTable.name]
        return channel
    }
}