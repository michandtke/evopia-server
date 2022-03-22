package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.TagDto
import de.mwa.evopiaserver.db.kotlin.DatabaseHelperMethods.orThrow
import de.mwa.evopiaserver.db.tag.Tag
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.bulkInsert
import org.springframework.stereotype.Component

@Component
class TagRepository(val databaseUtil: DatabaseUtil) {
    fun findAll(): List<TagDto> {
        val entries = databaseUtil.database.from(TagTable).select()
        return entries.map {
            TagDto(it.orThrow(TagTable.name))
        }
    }

    fun findByNameIn(tagNames: List<String>): List<Tag> {
        return databaseUtil.database
                .from(TagTable)
                .select()
                .where { (TagTable.name inList tagNames) }
                .map { rowToTag(it) }
    }

    private fun rowToTag(it: QueryRowSet): Tag =
            Tag(it[TagTable.id]?.toLong(), it[TagTable.name])

    fun save(tag: Tag): Int {
        return databaseUtil.database.insert(TagTable) {
            set(it.name, tag.name)
        }
    }

    fun saveAll(tags: List<TagDto>): Int {
        return databaseUtil.database.bulkInsert(TagTable) {
            tags.map {
                item {
                    set(TagTable.name, it.name)
                }
            }
        }
    }
}