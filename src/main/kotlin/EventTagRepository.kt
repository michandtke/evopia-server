package de.mwa.evopiaserver.db.kotlin

import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.springframework.stereotype.Component

@Component
class EventTagRepository(val databaseUtil: DatabaseUtil) {
    fun all(): List<EventTagDao> {
        return databaseUtil.database.from(EventTagTable)
            .select()
            .map { row -> EventTagTable.createEntity(row) }
    }

    fun insert(eventId: Int, tagIds: List<Int>): List<Any> {
        return  tagIds.map { tagId ->
            databaseUtil.database.insert(EventTagTable) {
                set(EventTagTable.eventId, eventId)
                set(EventTagTable.tagId, tagId)
            }
        }
    }
}