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

    fun connect(): Database {
        return Database.connect(appProperties.databaseUrl,
                user = appProperties.databaseUser,
                password = appProperties.databasePassword)
    }

    fun connect(url: String, user: String, password: String): Database {
        return Database.connect(url, user = user, password = password)
    }

    fun findAllChannels(database: Database): List<ChannelDto> {
        val entries = database.from(ChannelNew).select()
        return entries.map {
            ChannelDto(it[ChannelNew.name])
        }
    }

    fun saveChannel(database: Database, channel: ChannelDto): Int {
        return database.insert(ChannelNew) {
            set(it.name, channel.name)
        }
    }

    fun deleteChannelByName(database: Database, channelNameToDelete: String): Int {
        return database.delete(ChannelNew) {
            it.name eq channelNameToDelete
        }
    }
}