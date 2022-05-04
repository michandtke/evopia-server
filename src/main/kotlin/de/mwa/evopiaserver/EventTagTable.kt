package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface EventTagDao: Entity<EventTagDao> {
    companion object : Entity.Factory<EventTagDao>()
    var event: EventDao
    var tag: TagDao
}

object EventTagTable : Table<EventTagDao>("event_tags") {
    val eventId = int("event_id").primaryKey().references(EventTable) { it.event }
    val tagId = int("tag_id").primaryKey().references(TagTable) { it.tag }
}