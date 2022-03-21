package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.api.dto.UserTag
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertOrUpdate
import org.springframework.stereotype.Component

@Component
class UserTagRepository(
    val databaseUtil: DatabaseUtil,
    val userRepository: UserRepositoryNew,
    val tagRepository: TagRepository
) {
    fun selectFor(mail: String): List<UserTag> {
        return databaseUtil.database
            .from(UserTagTable)
            .innerJoin(UserTable, UserTable.id eq UserTagTable.userId)
            .innerJoin(TagTable, TagTable.id eq UserTagTable.tagId)
            .select()
            .where { (UserTable.email eq mail) }
            .map { rowToUserTag(it) }
    }

    private fun rowToUserTag(it: QueryRowSet): UserTag {
        return UserTag(
            it[TagTable.name]
        )
    }

    fun upsert(mail: String, newUserTags: List<UserTag>): Pair<Int, Int> {
        val userId = userRepository.findIdByMail(mail)
        val tagIds = tagRepository.findByNameIn(newUserTags.map { it.name }).map { it.id.toInt() }

        val deleted = deleteUserTags(tagIds, userId)

        val inserted = tagIds.map { insertOrUpdate(userId, it) }.fold(0) {a, b -> a + b}
        return Pair(inserted, deleted)
    }

    private fun deleteUserTags(tagIds: List<Int>, userId: Int): Int {
        if (tagIds.isEmpty()) return 0;
        val deleted = databaseUtil.database.delete(UserTagTable) {
            (it.tagId notInList tagIds) and (it.userId eq userId)
        }
        return deleted
    }

    private fun insertOrUpdate(userId: Int, tagId: Int): Int {
        return databaseUtil.database.insertOrUpdate(UserTagTable) {
            set(it.tagId, tagId)
            set(it.userId, userId)
        }
    }
}