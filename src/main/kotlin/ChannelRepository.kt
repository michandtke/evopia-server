package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.ChannelDto
import de.mwa.evopiaserver.db.kotlin.DatabaseHelperMethods.orThrow
import org.ktorm.database.Database
import org.ktorm.dsl.*

class ChannelRepository(val database: Database) {

    fun findAllChannels(): List<ChannelDto> {
        val entries = database.from(ChannelTable).select()
        return entries.map {
            rowToChannel(it)
        }
    }

    fun saveChannel(channel: ChannelDto): Int {
        return database.insert(ChannelTable) {
            set(it.name, channel.name)
        }
    }

    fun deleteChannelByName(channelNameToDelete: String): Int {
        return database.delete(ChannelTable) {
            it.name eq channelNameToDelete
        }
    }

    private fun rowToChannel(it: QueryRowSet): ChannelDto = ChannelDto(it.orThrow(ChannelTable.name))
}