package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver
import de.mwa.evopiaserver.db.kotlin.EventRepositoryNew
import de.mwa.evopiaserver.db.kotlin.EventTagRepository
import de.mwa.evopiaserver.db.kotlin.TagRepository
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import de.mwa.evopiaserver.routes.*
import io.ktor.server.application.*
import org.ktorm.database.Database

object WebServer {
    fun start(database: Database): NettyApplicationEngine {
        return embeddedServer(Netty, port = System.getenv("PORT")?.toInt()?:8080) {
            application(database, this)
        }
    }

    fun application(database: Database, app: Application) {
        val tagRepo = TagRepository(database)
        val eventTagRepo = EventTagRepository(database)
        val eventRepo = EventRepositoryNew(database, tagRepo, eventTagRepo)
        return app.configureRouting(eventRepo)
    }
}