package de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.*
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.insert

class RepositoryTestHelper(val databaseUtil: DatabaseUtil) {
    fun resetUserTable() {
        databaseUtil.database.deleteAll(UserTable)
        databaseUtil.database.insert(UserTable) {
            set(it.firstName, "Bruce")
            set(it.lastName, "Wayne")
            set(it.password, "\$2a\$11\$FwSqOARBYch53gfIev15ie9Sk0zC9i/gPJd/D1mLdwaeg13ui0NsG")
            set(it.email, "Batman@waynecorp.com")
        }
    }

    fun resetChannelTable() {
        databaseUtil.database.deleteAll(ChannelTable)
        databaseUtil.database.insert(ChannelTable) {
            set(it.name, "Dummychannel")
        }
    }

    fun resetUserChannelTable() {
        databaseUtil.database.deleteAll(UserChannelTable)
    }

    fun resetUserTagTable() {
        databaseUtil.database.deleteAll(UserTagTable)
    }

    fun resetTagTable() {
        databaseUtil.database.deleteAll(TagTable)
    }

    fun resetEventTable() {
        databaseUtil.database.deleteAll(EventTable)
    }

    fun resetEventTagsTable() {
        databaseUtil.database.deleteAll(EventTagTable)
    }

    fun addTag(name: String) {
        databaseUtil.database.insert(TagTable) {
            set(it.name, name)
        }
    }
}