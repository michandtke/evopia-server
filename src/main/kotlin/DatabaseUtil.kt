package de.mwa.evopiaserver.db.kotlin

import org.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DatabaseUtil {

    @Autowired
    lateinit var appProperties: AppProperties

    val database: Database by lazy {
        Database.connect(appProperties.databaseUrl,
                user = appProperties.databaseUser,
                password = appProperties.databasePassword)
    }
}