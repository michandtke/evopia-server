package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.EventDto
import de.mwa.evopiaserver.api.dto.TagDto
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.stereotype.Component


val Database.events get() = this.sequenceOf(EventTable)
val Database.eventTags get() = this.sequenceOf(EventTagTable)
val Database.tags get() = this.sequenceOf(TagTable)

@Component
class EventRepositoryNew(val databaseUtil: DatabaseUtil, val tagRepository: TagRepository, val eventTagRepository: EventTagRepository) {
    fun events(): List<EventDto> {
        val withTags = eventsWithTags()
        return withTags + eventsWithoutTags(withTags.mapNotNull { it.id })
    }

    private fun eventsWithTags(): List<EventDto> {
        val tagsByEvents = databaseUtil.database.eventTags
            .groupBy { it.event }
            .mapValues { it.value.map { it.tag } }

        return tagsByEvents.map { toEventDto(it.key, it.value) }
    }

    private fun eventsWithoutTags(knownEventIds: List<Int>): List<EventDto> {
        if (knownEventIds.isEmpty())
            return databaseUtil.database.events
                .map { toEventDto(it, emptyList()) }
        return databaseUtil.database.events
            .filter { it.id notInList knownEventIds }
            .map { toEventDto(it, emptyList()) }
    }

    private fun toEventDto(eventDao: EventDao, tags: List<TagDao>): EventDto {
        val eventId = eventDao.id
        return EventDto(
            id = eventId,
            name = eventDao.name,
            description = eventDao.description,
            date = eventDao.date,
            time = eventDao.time,
            place = eventDao.place,
            imagePath = eventDao.image,
            tags = tags.map { TagDto(it.name) }
        )
    }


    fun upsert(event: EventDto) {
        if (event.id != null) {
            return update(event)
        }
        return insert(event)
    }

    private fun insert(eventDto: EventDto) {
        val eventKey: Any = databaseUtil.database.insertAndGenerateKey(EventTable) {
            addColumnsExceptId(eventDto)
        }

        val saveAll = tagRepository.saveAll(eventDto.tags)
        eventTagRepository.insert(eventKey.toString().toInt(), saveAll)
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