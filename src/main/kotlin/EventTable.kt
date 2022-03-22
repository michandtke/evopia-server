package de.mwa.evopiaserver.db.kotlin

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EventTable : Table<Nothing>("event") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val description = varchar("description")
    val date = varchar("date")
    val time = varchar("time")
    val place = varchar("place")
    val image = varchar("image")
}