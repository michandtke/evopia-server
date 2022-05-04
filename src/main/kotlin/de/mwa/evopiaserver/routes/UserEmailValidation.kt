package de.mwa.evopiaserver.routes

import de.mwa.evopiaserver.service.UserServiceNew
import io.ktor.server.auth.*

fun userEmailValidation(credentials: UserPasswordCredential, userService: UserServiceNew): Principal? {
    val emailAddressRegex = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    if (!credentials.name.matches(emailAddressRegex)) return null

    val user = userService.find(credentials.name) ?: return null
    return UserIdPrincipal(user.email)
}