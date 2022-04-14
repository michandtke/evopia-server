package de.mwa.evopiaserver

import de.mwa.evopiaserver.api.dto.ChannelDto
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Delete
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


class ChannelAcceptanceTest : ServerTestSetup() {

    @AfterEach
    fun localCleanup() {
        repositoryTestHelper.resetChannelTable()
    }


    @Test
    fun initial_channel_has_only_dummychannel() = testApplication {
        val channels = getAllChannels()

        assertThat(channels).containsOnly(ChannelDto("Dummychannel"))
    }

    @Test
    fun adding_channel_should_work() = testApplication {
        val url = "http://localhost:8080/v3/channels/add"
        val body = "{\"name\": \"BatSign\"}"

        val client = HttpClient(CIO)
        val addResponse = client.request(url) {
            method = Post
            setBody(body)
        }

        assertThat(addResponse.status).isEqualTo(HttpStatusCode.OK)

        client.close()

        val channels = getAllChannels()
        assertThat(channels).containsOnly(ChannelDto("Dummychannel"), ChannelDto("BatSign"))
    }

    @Test
    fun removing_channel_should_work() = testApplication {
        val url = "http://localhost:8080/v3/channels/remove"
        val body = "{\"name\": \"Dummychannel\"}"
        val client = HttpClient(CIO)
        val addResponse = client.request(url) {
            method = Post
            setBody(body)
        }

        assertThat(addResponse.status).isEqualTo(HttpStatusCode.OK)

        client.close()

        val channelsAfterDelete = getAllChannels()
        assertThat(channelsAfterDelete).isEmpty()
    }

    private suspend fun getAllChannels(): List<ChannelDto> {
        val url = "http://localhost:8080/v3/channels"

        val client = HttpClient(CIO)
        val askResponse = client.request(url)

        assertThat(askResponse.status)
            .isEqualTo(HttpStatusCode.OK)

        client.close()

        return Json.decodeFromString(askResponse.bodyAsText())
    }
}
