package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes

import de.mwa.evopiaserver.api.NoRemoteUserFoundException
import de.mwa.evopiaserver.api.dto.UserTag
import de.mwa.evopiaserver.db.kotlin.UserTagRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.userTagRoutes(userTagRepository: UserTagRepository) {
    authenticate("auth-basic") {
        get("/v3/user/tags") {
            val name = call.userName() ?: throw NoRemoteUserFoundException("Too bad, no remote user found!")

            call.respond(userTagRepository.selectFor(name))
        }

        post("/v3/user/tags") {
            val userTagString = call.receive<String>()
            val userTagDtos = Json.decodeFromString<List<UserTag>>(userTagString)

            val name = call.userName() ?: throw NoRemoteUserFoundException("Too bad, no remote user found!")

            val upsertedDeleted = userTagRepository.upsert(name, userTagDtos)
            call.respondText("Upserted user tags: " + upsertedDeleted.first + " | Deleted user tags: " + upsertedDeleted.second)

        }
    }
}

/*
@GetMapping("/v2/user/tags")
    public List<UserTag> getUserTags(HttpServletRequest request) {
        return userTagRepository.selectFor(request.getRemoteUser());
    }

    @PostMapping("/v2/user/tags")
    public String upsertUserTags(@RequestBody List<UserTag> newUserTags, HttpServletRequest request) {
        Pair<Integer, Integer> upsertedDeleted = userTagRepository.upsert(request.getRemoteUser(), newUserTags);
        return "Upserted user tags: " + upsertedDeleted.getFirst() + " | Deleted user tags: " + upsertedDeleted.getSecond();
    }
 */