package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.EventDto
import de.mwa.evopiaserver.db.kotlin.DatabaseHelperMethods.orThrow
import org.ktorm.dsl.*
import org.springframework.stereotype.Component


@Component
class EventRepositoryNew(val databaseUtil: DatabaseUtil) {
    fun events(): List<EventDto> {
        return databaseUtil.database
            .from(EventTable)
            .select()
            .map { toEventDto(it) }
    }

    private fun toEventDto(it: QueryRowSet): EventDto = EventDto(
        id = it.orThrow(EventTable.id),
        name = it.orThrow(EventTable.name),
        description = it.orThrow(EventTable.description),
        date = it.orThrow(EventTable.date),
        time = it.orThrow(EventTable.time),
        place = it.orThrow(EventTable.place),
        imagePath = it.orThrow(EventTable.image),
    )

    fun upsert(event: EventDto) {
        if (event.id != null) {
            return update(event)
        }
        return insert(event)
    }

    private fun insert(event: EventDto) {
        databaseUtil.database.insertAndGenerateKey(EventTable) {
            addColumnsExceptId(event)
        }
    }

    private fun update(event: EventDto) {
        databaseUtil.database.update(EventTable) {
            addColumnsExceptId(event)
            set(it.id, event.id)
        }
    }

    private fun AssignmentsBuilder.addColumnsExceptId(event: EventDto) {
        set(EventTable.name, event.name)
        set(EventTable.date, event.date)
        set(EventTable.time, event.time)
        set(EventTable.place, event.place)
        set(EventTable.description, event.description)
        set(EventTable.image, event.imagePath)
    }

    fun delete(id: Int) = databaseUtil.database.delete(EventTable) {
        it.id eq id
    }
}