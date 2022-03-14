package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.ChannelDto
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

    fun findAllChannels(): List<ChannelDto> {
        val entries = database.from(ChannelNew).select()
        return entries.map {
            ChannelDto(it[ChannelNew.name])
        }
    }

    fun saveChannel(channel: ChannelDto): Int {
        return database.insert(ChannelNew) {
            set(it.name, channel.name)
        }
    }

    fun deleteChannelByName(channelNameToDelete: String): Int {
        return database.delete(ChannelNew) {
            it.name eq channelNameToDelete
        }
    }
}