package de.mwa.evopiaserver.db.kotlin

import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.ktorm.support.postgresql.PostgreSqlDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DatabaseUtil {

    @Autowired
    lateinit var appProperties: AppProperties

    val database by lazy {
        Database.connect(
            appProperties.databaseUrl,
            user = appProperties.databaseUser,
            password = appProperties.databasePassword,
            logger = ConsoleLogger(threshold = LogLevel.TRACE),
            dialect = PostgreSqlDialect()
        )
    }
}