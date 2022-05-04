package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes

import de.mwa.evopiaserver.api.NoRemoteUserFoundException
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.UpsertUserDto
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.registration.User
import de.mwa.evopiaserver.service.UserServiceNew
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.userRoutes(userService: UserServiceNew) {
    put("/v3/user") {
        val userString = call.receive<String>()
        val userDto = Json.decodeFromString<User>(userString)
        call.respond(userService.registerNewUserAccount(userDto))
    }
    authenticate("auth-basic") {
        get("/auth") {
            call.respondText("Hello, ${call.userName()}!")
        }
        get("/v3/user") {
            val name = call.userName() ?: throw NoRemoteUserFoundException("Too bad, no remote user found!")
            val user = userService.find(name)
            call.respond(user)
        }
        post("/v3/user") {
            val name = call.userName() ?: throw NoRemoteUserFoundException("Too bad, no remote user found!")
            val userString = call.receive<String>()
            val userDto = Json.decodeFromString<UpsertUserDto>(userString)
            val updatedCount = userService.update(userDto, name)
            call.respondText("Updated $updatedCount user.")
        }
    }
}

fun ApplicationCall.userName() : String? {
    return principal<UserIdPrincipal>()?.name
}