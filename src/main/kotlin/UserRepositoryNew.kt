package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.NoRemoteUserFoundException
import de.mwa.evopiaserver.api.dto.UpsertUserDto
import de.mwa.evopiaserver.db.kotlin.DatabaseHelperMethods.orThrow
import de.mwa.evopiaserver.registration.User
import org.ktorm.database.Database
import org.ktorm.dsl.*

class UserRepositoryNew(val database: Database) {
    fun findByEmail(mail: String): User? {
        return database
            .from(UserTable)
            .select()
            .where { (UserTable.email eq mail) }
            .map { rowToUser(it) }
            .firstOrNull()
    }

    fun findIdByMail(mail: String) : Int {
        return findByEmail(mail)?.id?.toInt() ?: throw NoRemoteUserFoundException("UserID not found.")
    }

    fun emailAlreadyExists(mail: String): Boolean {
        return database
            .from(UserTable)
            .select()
            .where { (UserTable.email eq mail) }
            .totalRecords > 0
    }

    private fun rowToUser(it: QueryRowSet): User {
        return User(
            id = it.orThrow(UserTable.id).toLong(),
            firstName = it.orThrow(UserTable.firstName),
            lastName = it.orThrow(UserTable.lastName),
            dateOfRegistration = it[UserTable.dateOfRegistration].orEmpty(),
            email = it.orThrow(UserTable.email),
            password = it.orThrow(UserTable.password),
            imagePath = it[UserTable.imagePath].orEmpty()
        )
    }

    fun save(user: User): Any =
        database.insertAndGenerateKey(UserTable) {
            set(it.firstName, user.firstName)
            set(it.lastName, user.lastName)
            set(it.password, user.password)
            set(it.email, user.email)
            set(it.imagePath, user.imagePath)
            set(it.dateOfRegistration, user.dateOfRegistration)
        }

    fun update(mail: String, upsertUser: UpsertUserDto): Int {
        return database.update(UserTable) {
            set(it.imagePath, upsertUser.imagePath)
            where { it.email eq mail }
        }
    }
}