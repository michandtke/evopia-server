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

    fun replaceAllForWith(mail: String, userChannel: MutableList<UserChannel>): Int {
        val toDelete: Iterable<QueryRowSet> = databaseUtil.database
            .from(UserChannelTable)
            .innerJoin(UserTable, UserTable.id eq UserChannelTable.userId)
            .innerJoin(ChannelTable, ChannelTable.id eq UserChannelTable.channelId)
            .select(UserChannelTable.channelId)
            .where { (UserTable.email eq mail) }
            .where { (ChannelTable.name inList userChannel.map { it.name }) }
            .groupBy(UserChannelTable.userId)
            .asIterable()


//        databaseUtil.database.delete(UserChannelTable) {
//            it[UserChannelTable.userId] eq toDelete[]
//        }

        TODO("Not yet implemented")
    }

    fun add(mail: String, channels: List<UserChannel>): Int {
        val userId = databaseUtil.database.from(UserTable)
            .select(UserTable.id)
            .where(UserTable.email eq mail)
            .map { it[UserTable.id] }
            .first()

        val channelIdValue: List<Pair<Int?, String>> = databaseUtil.database.from(ChannelTable)
            .select()
            .where(ChannelTable.name inList channels.map { it.name })
            .map { Pair(it[ChannelTable.id], channels.first { chan -> chan.name == it[ChannelTable.name] }.value) }

//        var x = databaseUtil.database
//            .from(UserChannelTable)
//            .innerJoin(UserTable, UserTable.id eq UserChannelTable.userId)
//            .innerJoin(ChannelTable, ChannelTable.id eq UserChannelTable.channelId)
//            .select(UserChannelTable.channelId, UserChannelTable.userId)
//            .where { (UserTable.email eq mail) }
//            .where { (ChannelTable.name inList channels.map { it.name }) }
//            .map { Pair(it[UserChannelTable.channelId], it[UserChannelTable.userId]) }

        return databaseUtil.database.batchInsert(UserChannelTable) {
            channelIdValue.map { idValuePair ->
                item {
                    set(it.channelId, idValuePair.first)
                    set(it.userId, userId)
                    set(it.value, idValuePair.second)
                }
            }
        }.reduce { a: Int, b: Int -> a + b }
    }
}