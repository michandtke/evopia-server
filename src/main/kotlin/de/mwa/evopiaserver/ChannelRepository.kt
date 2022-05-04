package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.ChannelDto
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.DatabaseHelperMethods.orThrow
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
            set(ChannelTable.name, channel.name)
        }
    }

    fun deleteChannelByName(channelNameToDelete: String): Int {
        return database.delete(ChannelTable) {
            ChannelTable.name eq channelNameToDelete
        }
    }

    private fun rowToChannel(it: QueryRowSet): ChannelDto = ChannelDto(it.orThrow(ChannelTable.name))
}