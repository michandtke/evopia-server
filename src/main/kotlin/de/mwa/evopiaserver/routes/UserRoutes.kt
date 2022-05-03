package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.routes

import de.mwa.evopiaserver.api.NoRemoteUserFoundException
import de.mwa.evopiaserver.service.UserServiceNew
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserServiceNew) {
    authenticate("auth-basic") {
        get("/auth") {
            call.respondText("Hello, ${call.userName()}!")
        }
        get("/v3/user") {
            val name = call.userName() ?: throw NoRemoteUserFoundException ("Too bad, no remote user found!")
            val user = userService.find(name)
            call.respond(user)
        }
        put("/v3/user") {

        }
        post("/v3/user") {

        }
    }


}

fun ApplicationCall.userName() : String? {
    return principal<UserIdPrincipal>()?.name
}


/*
@GetMapping("/v2/user")
    public User getUser(final HttpServletRequest request) {
        if (request.getRemoteUser() == null)
            throw new NoRemoteUserFoundException("Too bad, no remote user found!");
        return userService.find(request.getRemoteUser());
    }

    @PutMapping("/v2/user")
    public User addNewUser(@RequestBody User newUser) {
        return userService.registerNewUserAccount(newUser);

    }

    @PostMapping("/v2/user")
    public String upsertUser(@RequestBody UpsertUserDto upsertUser, HttpServletRequest request) {
        if (request.getRemoteUser() == null)
            throw new NoRemoteUserFoundException("Too bad, no remote user found!");
        var updatedCount = userService.update(upsertUser, request.getRemoteUser());
        return "Updated " + updatedCount + " user.";
    }
 */