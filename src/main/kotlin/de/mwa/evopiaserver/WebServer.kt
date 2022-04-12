package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver
import de.mwa.evopiaserver.db.kotlin.DatabaseUtil
import de.mwa.evopiaserver.db.kotlin.EventRepositoryNew
import de.mwa.evopiaserver.db.kotlin.EventTagRepository
import de.mwa.evopiaserver.db.kotlin.TagRepository
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import de.mwa.evopiaserver.routes.*

object WebServer {
    val server = embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        val config = ConfigProvider().config()
        val database = DatabaseUtil(config).db()
        val tagRepo = TagRepository(database)
        val eventTagRepo = EventTagRepository(database)
        val eventRepo = EventRepositoryNew(database, tagRepo, eventTagRepo)
        configureRouting(eventRepo)
    }

    fun start() {
        server.start(wait = true)
    }
}