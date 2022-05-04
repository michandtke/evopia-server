package de.mwa.evopiaserver

import com.sksamuel.hoplite.ConfigException
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.WebServer.start
import kotlin.jvm.JvmStatic
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.ConfigProvider
import de.mwa.evopiaserver.db.kotlin.DatabaseUtil

object EvopiaServerApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val config = ConfigProvider().config()
            val database = DatabaseUtil(config).db()
            start(database).start(true)
        } catch (e: ConfigException) {
            println("Could not load config: " + e.message)
        }
    }
}