package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.TagDto
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.DatabaseHelperMethods.orThrow
import org.ktorm.database.Database
import org.ktorm.dsl.*

class TagRepository(val database: Database) {
    fun findAll(): List<TagDto> {
        val entries = database.from(TagTable).select()
        return entries.map {
            TagDto(it.orThrow(TagTable.name))
        }
    }

    fun findByNameIn(tagNames: List<String>): List<TagDao> {
        return database
                .from(TagTable)
                .select()
                .where { (TagTable.name inList tagNames) }
                .map { TagTable.createEntity(it) }
    }

    fun saveAll(tags: List<TagDto>): List<Int> {
        return tags.map { tag ->
            database.insertAndGenerateKey(TagTable) {
                set(TagTable.name, tag.name)
            }
        }.mapNotNull { it as? Int }

    }
}