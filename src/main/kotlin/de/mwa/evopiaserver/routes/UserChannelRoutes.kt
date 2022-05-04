package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes

import de.mwa.evopiaserver.api.NoRemoteUserFoundException
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.UserChannel
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.service.ChannelService
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.service.UserChannelService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.userChannelRoutes(userChannelService: UserChannelService, channelService: ChannelService) {
    authenticate("auth-basic") {
        get("/v3/user/channel") {
            val name = call.userName() ?: throw NoRemoteUserFoundException("Too bad, no remote user found!")

            call.respond(userChannelService.getChannels(name))
        }

        post("/v3/user/channel") {
            val userChannelString = call.receive<String>()
            val userChannelDtos = Json.decodeFromString<List<UserChannel>>(userChannelString)

            val name = call.userName() ?: throw NoRemoteUserFoundException("Too bad, no remote user found!")

            val unknown = channelService.unknownChannelNames(userChannelDtos.map { it.name })
            if (unknown.isNotEmpty()) {
                val message = "Unknown channel: " + unknown.joinToString()
                call.respondText(message, status = HttpStatusCode.BadRequest)
            }

            val added = userChannelService.add(name, userChannelDtos)
            call.respondText("Successfully replaced channels. Added : " + added.first + " | Deleted: " + added.second)

        }
    }
}