package de.mwa.evopiaserver.db.kotlin

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.Config
import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.ktorm.support.postgresql.PostgreSqlDialect
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DatabaseUtil(val config: Config) {

    val database by lazy {
        Database.connect(
            config.database.url,
            user = config.database.username,
            password = config.database.password,
            logger = ConsoleLogger(threshold = LogLevel.TRACE),
            dialect = PostgreSqlDialect()
        )
    }

    @Bean
    fun db(): Database {
        return database
    }
}