package de.mwa.evopiaserver

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserChannelTable : Table<Nothing>("userchannel") {
    val userId = int("user_id").primaryKey()
    val channelId = int("channel_id").primaryKey()
    val value = varchar("value")
}