package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.EventDto
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.TagDto
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.filter
import org.ktorm.entity.groupBy
import org.ktorm.entity.map
import org.ktorm.entity.sequenceOf

val Database.events get() = this.sequenceOf(EventTable)
val Database.eventTags get() = this.sequenceOf(EventTagTable)
val Database.tags get() = this.sequenceOf(TagTable)

class EventRepositoryNew(
    val database: Database,
    val tagRepository: TagRepository,
    val eventTagRepository: EventTagRepository
) {
    fun events(): List<EventDto> {
        val withTags = eventsWithTags()
        return withTags + eventsWithoutTags(withTags.mapNotNull { it.id })
    }

    private fun eventsWithTags(): List<EventDto> {
        val tagsByEvents = database.eventTags
            .groupBy { it.event }
            .mapValues { it.value.map { it.tag } }

        return tagsByEvents.map { toEventDto(it.key, it.value) }
    }

    private fun eventsWithoutTags(knownEventIds: List<Int>): List<EventDto> {
        if (knownEventIds.isEmpty())
            return database.events
                .map { toEventDto(it, emptyList()) }
        return database.events
            .filter { it.id notInList knownEventIds }
            .map { toEventDto(it, emptyList()) }
    }

    private fun toEventDto(eventDao: EventDao, tags: List<TagDao>): EventDto {
        val eventId = eventDao.id
        return EventDto(
            id = eventId,
            name = eventDao.name,
            description = eventDao.description,
            from = eventDao.date,
            to = eventDao.time,
            place = eventDao.place,
            imagePath = eventDao.image,
            tags = tags.map { TagDto(it.name) }
        )
    }


    fun upsert(event: EventDto) {
        if (event.id != -1) {
            return update(event)
        }
        return insert(event)
    }

    private fun insert(event: EventDto) {
        val eventKey: Any = database.insertAndGenerateKey(EventTable) {
            addColumnsExceptId(event)
        }

        upsertEventTags(eventKey.toString().toInt(), event.tags)
    }

    private fun update(event: EventDto) {
        database.update(EventTable) {
            addColumnsExceptId(event)
            where { it.id eq event.id }
        }
        upsertEventTags(event.id, event.tags)
    }

    private fun upsertEventTags(eventId: Int, tags: List<TagDto>) {
        val saveAll = tagRepository.saveAll(tags)
        eventTagRepository.insert(eventId, saveAll)
    }

    private fun AssignmentsBuilder.addColumnsExceptId(event: EventDto) {
        set(EventTable.name, event.name)
        set(EventTable.date, event.from)
        set(EventTable.time, event.to)
        set(EventTable.place, event.place)
        set(EventTable.description, event.description)
        set(EventTable.image, event.imagePath)
    }

    fun delete(id: Int) = database.delete(EventTable) {
        it.id eq id
    }
}