package de.mwa.evopiaserver

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserTable : Table<Nothing>("useraccount") {
    val id = int("id").primaryKey()
    val firstName = varchar("first_name")
    val lastName = varchar("last_name")
    val dateOfRegistration = varchar("date_of_registration")
    val email = varchar("email")
    val password = varchar("password")
    val imagePath = varchar("image_path")
}