package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.NoRemoteUserFoundException
import de.mwa.evopiaserver.api.dto.UserChannel
import de.mwa.evopiaserver.profile.ProfileChannel
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.bulkInsertOrUpdate
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
            .innerJoin(ChannelTable, ChannelTable.id eq UserChannelTable.channelId)
            .select()
            .where { (UserTable.email eq email) }
            .map { rowToUserChannel(it) }
    }

    private fun rowToUserChannel(it: QueryRowSet): UserChannel {
        return UserChannel(
            it[ChannelTable.name],
            it[UserChannelTable.value]
        )
    }

    fun add(mail: String, channels: List<UserChannel>): Pair<Int, Int> {

        val userId = getUserIdFor(mail) ?: throw NoRemoteUserFoundException("UserID not found.")

        val channelIdValue: List<Pair<Int?, String?>> = getExistingUserChannels(userId, channels)
        var channelIds: List<Pair<Int, String>> = getChannelIds()


        val grouped = channelIdValue.groupBy { it.second != null }

        val maybeToInsert = grouped[true]
        val inserted = if (maybeToInsert != null) { bulkInsertOrUpdate(maybeToInsert, userId) } else 0

        val toDelete = grouped[false]?.mapNotNull { it.first }

        val deletedCount = delete(toDelete, userId)

        return Pair(inserted, deletedCount)
    }

    private fun getChannelIds(): List<Pair<Int, String>> =
        databaseUtil.database.from(ChannelTable)
            .select()
            .mapNotNull {
                val channelId = it[ChannelTable.id]
                val channelName = it[ChannelTable.name]
                if (channelId != null && channelName != null)
                    Pair(channelId, channelName)
                else
                    null
            }

    private fun getExistingUserChannels(userId: Int, channels: List<UserChannel>): List<Pair<Int?, String?>> {
        return databaseUtil.database.from(UserChannelTable)
            .rightJoin(ChannelTable, (ChannelTable.id eq UserChannelTable.channelId) and (UserChannelTable.userId eq userId))
            .select()
            .map {
                Pair(
                    it[ChannelTable.id],
                    channels.firstOrNull { chan -> chan.name == it[ChannelTable.name] }?.value
                )
            }

//        return databaseUtil.database.from(ChannelTable)
//            .select()
//            .map {
//                Pair(
//                    it[ChannelTable.id],
//                    channels.firstOrNull { chan -> chan.name == it[ChannelTable.name] }?.value
//                )
//            }
    }

    private fun getUserIdFor(mail: String): Int? {
        return databaseUtil.database.from(UserTable)
            .select(UserTable.id)
            .where(UserTable.email eq mail)
            .map { it[UserTable.id] }
            .first()
    }

    private fun delete(toDelete: List<Int>?, userId: Int): Int {
        if (toDelete != null)
            return databaseUtil.database.delete(UserChannelTable) {
                (it.channelId inList toDelete) and (it.userId eq userId)
            }
        return 0
    }

    private fun bulkInsertOrUpdate(
        toInsert: List<Pair<Int?, String?>>,
        userId: Int
    ): Int {
        return databaseUtil.database.bulkInsertOrUpdate(UserChannelTable) {
            toInsert.map { idValuePair ->
                item {
                    set(it.channelId, idValuePair.first)
                    set(it.userId, userId)
                    set(it.value, idValuePair.second)
                }
                onConflict {
                    set(it.value, idValuePair.second)
                }
            }
        }
    }
}