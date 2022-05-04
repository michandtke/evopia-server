package de.mwa.evopiaserver.routes

import de.mwa.evopiaserver.dto.EventDto
import de.mwa.evopiaserver.EventRepositoryNew
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.eventRoutes(eventRepo: EventRepositoryNew) {
    get("/v3/events") {
        val events = eventRepo.events()
        call.respond(events)
    }
    post("/v3/events/upsert") {
        val eventString = call.receive<String>()
        val event = Json.decodeFromString<EventDto>(eventString)

        println(eventString)
        eventRepo.upsert(event)
        call.respond("Upserted")
    }
    delete("/v3/events/{id}") {
        val id = call.parameters["id"]
        if (id != null) {
            val result: Int = eventRepo.delete(id.toInt())
            call.respond("Deleted " + result + "events with id " + id)
        }
        call.respond("We need an id to delete something")
    }
}