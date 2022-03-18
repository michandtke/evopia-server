package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.UserChannel
import de.mwa.evopiaserver.profile.ProfileChannel
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

@Component
class UserChannelRepositoryNew(val databaseUtil: DatabaseUtil) {
    fun saveAll(profileChannelUpserts: MutableList<ProfileChannel>): Int {
        return databaseUtil.database.batchInsert(UserChannelTable) {
            profileChannelUpserts.map { chan ->
                item {
                    set(it.channelId, chan.channelId)
                    set(it.userId, chan.userId)
                    set(it.value, chan.value)
                }
            }
        }.reduce { a: Int, b: Int -> a + b }

    }

    fun findForUser(email: String): List<UserChannel> {
        return databaseUtil.database
            .from(UserChannelTable)
            .innerJoin(UserTable, UserTable.id eq UserChannelTable.userId)
            .select()
            .where { (UserTable.email eq email) }
            .map { rowToUserChannel(it) }
    }

    private fun rowToUserChannel(it: QueryRowSet): UserChannel {
        return UserChannel(
            it[UserTable.email],
            it[UserChannelTable.value]
        )
    }
}