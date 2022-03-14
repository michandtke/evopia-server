package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.db.tag.Tag
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

@Component
class TagRepository(val databaseUtil: DatabaseUtil) {
    fun findAll(): List<Tag> {
        val entries = databaseUtil.database.from(TagTable).select()
        return entries.map {
            rowToTag(it)
        }
    }

    fun findByNameIn(tagNames: MutableList<String>): List<Tag> {
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
}