package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface TagDao : Entity<TagDao> {
    companion object : Entity.Factory<TagDao>()
    val id: Int
    var name: String
}

object TagTable : Table<TagDao>("tag") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}