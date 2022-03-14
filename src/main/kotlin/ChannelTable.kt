package de.mwa.evopiaserver.db.kotlin

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object ChannelTable : Table<Nothing>("channel") {
    val id = int("id").primaryKey()
    val name = varchar("name")
}