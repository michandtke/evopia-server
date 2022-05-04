package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.TagDto
import de.mwa.evopiaserver.db.kotlin.TagRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.tagRoutes(tagRepository: TagRepository) {

    get("/v3/tags") {
        call.respond(tagRepository.findAll())
    }
    post("/v3/tags/add") {
        val tagsString = call.receive<String>()
        val tagDtos = Json.decodeFromString<List<TagDto>>(tagsString)

        val insertedKeys = tagRepository.saveAll(tagDtos)
        call.respond("Inserted ${insertedKeys.size} tags.")
    }
}