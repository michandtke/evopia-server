package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import de.mwa.evopiaserver.routes.*

object WebServer {
    val server = embeddedServer(Netty, port = 8090, host = "0.0.0.0") {
        configureRouting()
    }

    fun start() {
        server.start(wait = true)
    }
}