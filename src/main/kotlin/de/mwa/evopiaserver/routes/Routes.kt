package de.mwa.evopiaserver.routes

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import de.mwa.evopiaserver.db.kotlin.EventRepositoryNew
import de.mwa.evopiaserver.db.kotlin.TagRepository
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes.*
import de.mwa.evopiaserver.service.ChannelService
import de.mwa.evopiaserver.service.UserServiceNew
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    eventRepo: EventRepositoryNew,
    channelService: ChannelService,
    tagRepository: TagRepository,
    userService: UserServiceNew
) {
    install(ContentNegotiation) {
        jackson {
            this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }
    install(Authentication) {
        basic("auth-basic") {
            validate { userEmailValidation(it, userService) }
        }
    }
    routing {
        get("/health") {
            call.respondText("Hello World!")
        }
        eventRoutes(eventRepo)
        channelRoutes(channelService)
        tagRoutes(tagRepository)
        userRoutes(userService)
    }
}