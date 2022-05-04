package de.mwa.evopiaserver

import de.mwa.evopiaserver.dto.UserTag
import de.mwa.evopiaserver.DatabaseHelperMethods.orThrow
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertOrUpdate

class UserTagRepository(
    val database: Database,
    val userRepository: UserRepositoryNew,
    val tagRepository: TagRepository
) {
    fun selectFor(mail: String): List<UserTag> {
        return database
            .from(UserTagTable)
            .innerJoin(UserTable, UserTable.id eq UserTagTable.userId)
            .innerJoin(TagTable, TagTable.id eq UserTagTable.tagId)
            .select()
            .where { (UserTable.email eq mail) }
            .map { rowToUserTag(it) }
    }

    private fun rowToUserTag(it: QueryRowSet): UserTag {
        return UserTag(
            it.orThrow(TagTable.name)
        )
    }

    fun upsert(mail: String, newUserTags: List<UserTag>): Pair<Int, Int> {
        val userId = userRepository.findIdByMail(mail)
        val tagIds = tagRepository.findByNameIn(newUserTags.map { it.name }).map { it.id }

        val deleted = deleteUserTags(tagIds, userId)

        val inserted = tagIds.map { insertOrUpdate(userId, it) }.fold(0) {a, b -> a + b}
        return Pair(inserted, deleted)
    }

    private fun deleteUserTags(tagIds: List<Int>, userId: Int): Int {
        if (tagIds.isEmpty()) return 0;
        val deleted = database.delete(UserTagTable) {
            (UserTagTable.tagId notInList tagIds) and (UserTagTable.userId eq userId)
        }
        return deleted
    }

    private fun insertOrUpdate(userId: Int, tagId: Int): Int {
        return database.insertOrUpdate(UserTagTable) {
            set(UserTagTable.tagId, tagId)
            set(UserTagTable.userId, userId)
            onConflict {
                doNothing()
            }
        }
    }
}