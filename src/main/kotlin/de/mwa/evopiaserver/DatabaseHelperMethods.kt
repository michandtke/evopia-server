package de.mwa.evopiaserver

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.Column

object DatabaseHelperMethods {
    fun <C : Any> QueryRowSet.orThrow(column: Column<C>): C = get(column) ?: throw IllegalArgumentException("Column unknown: $column")
}