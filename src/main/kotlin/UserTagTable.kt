package de.mwa.evopiaserver.db.kotlin

import org.ktorm.schema.Table
import org.ktorm.schema.int

object UserTagTable : Table<Nothing>("user_tags") {
    val userId = int("user_id").primaryKey()
    val channelId = int("tag_id").primaryKey()
}