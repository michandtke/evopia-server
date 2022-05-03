package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver

import com.sksamuel.hoplite.ConfigLoader

open class ConfigProvider {
    open fun config(): Config {
        return ConfigLoader().loadConfigOrThrow<Config>("/config-local.yml")
    }
}