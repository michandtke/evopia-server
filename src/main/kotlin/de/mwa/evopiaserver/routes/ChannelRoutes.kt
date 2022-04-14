package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes

import de.mwa.evopiaserver.api.dto.ChannelDto
import de.mwa.evopiaserver.service.ChannelService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.channelRoutes(channelService: ChannelService) {
    get("/v3/channels") {
        call.respond(channelService.findAll())
    }
    post("/v3/channels/add") {
        val asString = call.receive<String>()
        val channelToAdd = Json.decodeFromString<ChannelDto>(asString)
        val count = channelService.add(channelToAdd)
        call.respond("Added $count entries.")
    }
    post("/v3/channels/remove") {
        val asString = call.receive<String>()
        val channelToDelete = Json.decodeFromString<ChannelDto>(asString)
        val count = channelService.remove(channelToDelete)
        call.respond("Deleted $count entries.")
    }
}