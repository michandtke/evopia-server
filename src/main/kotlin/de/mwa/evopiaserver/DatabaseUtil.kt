package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.Config
import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.ktorm.support.postgresql.PostgreSqlDialect

class DatabaseUtil(val config: Config) {

    val database by lazy {
        Database.connect(
            config.database.url,
            logger = ConsoleLogger(threshold = LogLevel.TRACE),
            dialect = PostgreSqlDialect()
        )
    }

    fun db(): Database {
        return database
    }
}