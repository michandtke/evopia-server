package de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.ChannelTable
import de.mwa.evopiaserver.db.kotlin.DatabaseUtil
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.insert
import org.springframework.stereotype.Component

@Component
class ChannelRepositoryTestHelper(val databaseUtil: DatabaseUtil) {
    fun resetForTests() {
        databaseUtil.database.deleteAll(ChannelTable)
        databaseUtil.database.insert(ChannelTable) {
            set(it.name, "Dummychannel")
        }
    }
}