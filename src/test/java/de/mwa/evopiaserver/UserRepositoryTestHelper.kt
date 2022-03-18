package de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.DatabaseUtil
import de.mwa.evopiaserver.db.kotlin.UserTable
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.insert
import org.springframework.stereotype.Component

@Component
class UserRepositoryTestHelper(val databaseUtil: DatabaseUtil) {
    fun resetForTests() {
        databaseUtil.database.deleteAll(UserTable)
        databaseUtil.database.insert(UserTable) {
            set(it.firstName, "Bruce")
            set(it.lastName, "Wayne")
            set(it.password, "\$2a\$11\$FwSqOARBYch53gfIev15ie9Sk0zC9i/gPJd/D1mLdwaeg13ui0NsG")
            set(it.email, "Batman@waynecorp.com")
        }
    }
}