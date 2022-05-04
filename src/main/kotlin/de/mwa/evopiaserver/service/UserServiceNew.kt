package de.mwa.evopiaserver.service

import at.favre.lib.crypto.bcrypt.BCrypt
import de.mwa.evopiaserver.UserRepositoryNew
import kotlin.Throws
import de.mwa.evopiaserver.registration.UserAlreadyExistsException
import de.mwa.evopiaserver.dto.UpsertUserDto
import de.mwa.evopiaserver.registration.User

class UserServiceNew(private val userRepository: UserRepositoryNew) {
    @Throws(UserAlreadyExistsException::class)
    fun registerNewUserAccount(user: User): User {
        if (userRepository.emailAlreadyExists(user.email)) {
            throw UserAlreadyExistsException(
                "There is an account with that email address: "
                        + user.email
            )
        }
        val encrypted = BCrypt.withDefaults().hashToString(12, user.password.toCharArray())
        user.password = encrypted
        userRepository.save(user)
        return user
    }

    fun find(mail: String?): User? {
        return userRepository.findByEmail(mail!!)
    }

    fun update(upsertUser: UpsertUserDto?, mail: String?): Int {
        return userRepository.update(mail!!, upsertUser!!)
    }
}