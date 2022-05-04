package de.mwa.evopiaserver.db.kotlin

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.support.postgresql.bulkInsertOrUpdate

class EventTagRepository(val database: Database) {
    fun all(): List<EventTagDao> {
        return database.from(EventTagTable)
            .select()
            .map { row -> EventTagTable.createEntity(row) }
    }

    fun insert(eventId: Int, tagIds: List<Int>): Int {
        if (tagIds.isEmpty()) return -1

        return database.bulkInsertOrUpdate(EventTagTable) {
            tagIds.map {
                item {
                    set(EventTagTable.eventId, eventId)
                    set(EventTagTable.tagId, it)
                }
            }
            onConflict {
                doNothing()
            }
        }
    }
}