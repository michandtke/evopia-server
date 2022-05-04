package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver

import org.ktorm.schema.Table
import org.ktorm.schema.int

object UserTagTable : Table<Nothing>("user_tags") {
    val userId = int("user_id").primaryKey()
    val tagId = int("tag_id").primaryKey()
}