package de.mwa.evopiaserver.db.kotlin

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserChannelTable : Table<Nothing>("profilechannels") {
    val userId = int("profile_id").primaryKey()
    val channelId = int("channel_id").primaryKey()
    val value = varchar("value")
}