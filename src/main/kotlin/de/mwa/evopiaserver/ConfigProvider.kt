package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver

import com.sksamuel.hoplite.ConfigLoader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ConfigProvider {
    @Bean
    open fun config(): Config {
        return ConfigLoader().loadConfigOrThrow<Config>("/config-local.yml")
    }
}