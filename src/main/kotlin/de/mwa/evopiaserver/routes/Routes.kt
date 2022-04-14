package de.mwa.evopiaserver.routes

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import de.mwa.evopiaserver.api.dto.EventDto
import de.mwa.evopiaserver.db.kotlin.EventRepositoryNew
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

fun Application.configureRouting(eventRepo: EventRepositoryNew) {
    install(ContentNegotiation) {
        jackson {
            this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }
    routing {
        get("/health") {
            call.respondText("Hello World!")
        }
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
}