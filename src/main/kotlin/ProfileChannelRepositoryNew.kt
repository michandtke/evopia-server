package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.profile.ProfileChannel
import org.ktorm.dsl.batchInsert
import org.springframework.stereotype.Component

@Component
class ProfileChannelRepositoryNew(val databaseUtil: DatabaseUtil) {
    fun saveAll(profileChannelUpserts: MutableList<ProfileChannel>): Int {
        return databaseUtil.database.batchInsert(ProfileChannelTable) {
            profileChannelUpserts.map { chan ->
                item {
                    set(it.channelId, chan.channelId)
                    set(it.userId, chan.userId)
                    set(it.value, chan.value)
                }
            }
        }.reduce { a: Int, b: Int -> a + b }

    }
}