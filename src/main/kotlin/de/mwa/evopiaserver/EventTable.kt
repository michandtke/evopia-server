package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface EventDao: Entity<EventDao> {
    companion object : Entity.Factory<EventDao>()
    val id: Int
    var name: String
    var description: String
    var date: String
    var time: String
    var place: String
    var image: String
}

object EventTable : Table<EventDao>("event") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val description = varchar("description").bindTo { it.description }
    val date = varchar("date").bindTo { it.date }
    val time = varchar("time").bindTo { it.time }
    val place = varchar("place").bindTo { it.place }
    val image = varchar("image").bindTo { it.image }
}