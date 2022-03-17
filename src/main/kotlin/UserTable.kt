package de.mwa.evopiaserver.db.kotlin

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserTable : Table<Nothing>("useraccount") {
    val id = int("id").primaryKey()
    val firstName = varchar("firstName")
    val lastName = varchar("lastName")
    val dateOfRegistration = varchar("dateOfRegistration")
    val email = varchar("email")
    val password = varchar("password")
    val imagePath = varchar("imagePath")
}