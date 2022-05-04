package de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.WebServer.start
import kotlin.jvm.JvmStatic
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.ConfigProvider
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.DatabaseUtil

object EvopiaServerApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val config = ConfigProvider().config()
            val database = DatabaseUtil(config).db()
            start(database).start(true)
        } catch (e: Exception) {
            println("Could not load config: " + e.message)
        }
    }
}