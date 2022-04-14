package de.mwa.evopiaserver.routes

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import de.mwa.evopiaserver.api.dto.ChannelDto
import de.mwa.evopiaserver.api.dto.EventDto
import de.mwa.evopiaserver.db.kotlin.EventRepositoryNew
import de.mwa.evopiaserver.db.kotlin.TagRepository
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes.channelRoutes
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes.eventRoutes
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes.tagRoutes
import de.mwa.evopiaserver.service.ChannelService
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

fun Application.configureRouting(eventRepo: EventRepositoryNew, channelService: ChannelService, tagRepository: TagRepository) {
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
        eventRoutes(eventRepo)
        channelRoutes(channelService)
        tagRoutes(tagRepository)
    }
}