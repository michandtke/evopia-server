package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.registration.User
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

@Component
class UserRepositoryNew(val databaseUtil: DatabaseUtil) {
    fun findByEmail(mail: String): User? {
        return databaseUtil.database
            .from(UserTable)
            .select()
            .where { (UserTable.email eq mail) }
            .map { rowToUser(it) }
            .firstOrNull()
    }

    fun emailAlreadyExists(mail: String): Boolean {
        return databaseUtil.database
            .from(UserTable)
            .select()
            .where { (UserTable.email eq mail) }
            .totalRecords > 0
    }

    private fun rowToUser(it: QueryRowSet): User {
        return User(
            it[UserTable.id]?.toLong(),
            it[UserTable.firstName],
            it[UserTable.lastName],
            it[UserTable.dateOfRegistration],
            it[UserTable.email],
            it[UserTable.password],
            it[UserTable.imagePath]
        )
    }

    fun save(user: User): Int =
        databaseUtil.database.insert(UserTable) {
            set(it.firstName, user.firstName)
            set(it.lastName, user.lastName)
            set(it.password, user.password)
            set(it.email, user.email)
            set(it.imagePath, user.imagePath)
        }
}