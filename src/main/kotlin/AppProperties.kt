package de.mwa.evopiaserver.db.kotlin

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {
    @Value("\${spring.datasource.url}")
    lateinit var databaseUrl: String
    @Value("\${spring.datasource.username}")
    lateinit var databaseUser: String
    @Value("\${spring.datasource.password}")
    lateinit var databasePassword: String
}

