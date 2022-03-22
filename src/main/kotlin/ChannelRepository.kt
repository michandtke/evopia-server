package de.mwa.evopiaserver.db.kotlin

    import de.mwa.evopiaserver.api.dto.ChannelDto
import de.mwa.evopiaserver.db.kotlin.DatabaseHelperMethods.orThrow
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

@Component
class ChannelRepository(val databaseUtil: DatabaseUtil) {

    fun findAllChannels(): List<ChannelDto> {
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

    private fun rowToChannel(it: QueryRowSet): ChannelDto = ChannelDto(it.orThrow(ChannelTable.name))
}