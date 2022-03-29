package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.TagDto
import de.mwa.evopiaserver.db.kotlin.DatabaseHelperMethods.orThrow
import de.mwa.evopiaserver.db.tag.Tag
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

@Component
class TagRepository(val databaseUtil: DatabaseUtil) {
    fun findAll(): List<TagDto> {
        val entries = databaseUtil.database.from(TagTable).select()
        return entries.map {
            TagDto(it.orThrow(TagTable.name))
        }
    }

    fun findByNameIn(tagNames: List<String>): List<TagDao> {
        return databaseUtil.database
                .from(TagTable)
                .select()
                .where { (TagTable.name inList tagNames) }
                .map { TagTable.createEntity(it) }
    }

    fun save(tag: Tag): Int {
        return databaseUtil.database.insert(TagTable) {
            set(it.name, tag.name)
        }
    }

    fun saveAll(tags: List<TagDto>): List<Int> {
        return tags.map { tag ->
            databaseUtil.database.insertAndGenerateKey(TagTable) {
                set(TagTable.name, tag.name)
            }
        }.mapNotNull { it as? Int }

    }
}