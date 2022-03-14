package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.ChannelDto
import de.mwa.evopiaserver.db.channel.Channel
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DatabaseWrapper {

    @Autowired
    lateinit var appProperties: AppProperties

    private val database: Database by lazy {
        Database.connect(appProperties.databaseUrl,
                user = appProperties.databaseUser,
                password = appProperties.databasePassword)
    }

    fun findAllChannels(): List<Channel> {
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

    fun findChannelsByNameIn(channelNames: MutableList<String>): List<Channel> {
        return database
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