package de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.ChannelDto
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Delete
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.server.testing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@OptIn(ExperimentalCoroutinesApi::class)
class ChannelAcceptanceTest : ServerTestSetup() {

    @AfterEach
    fun localCleanup() {
        repositoryTestHelper.resetChannelTable()
    }


    @Test
    fun initial_channel_has_only_dummychannel() = runTest {
        val channels = getAllChannels()

        assertThat(channels).containsOnly(ChannelDto("Dummychannel"))
    }

    @Test
    fun adding_channel_should_work() = runTest {
        val url = "http://localhost:8080/v3/channels/add"
        val body = "{\"name\": \"BatSign\"}"

        val addResponse = client.request(url) {
            method = Post
            setBody(body)
        }

        assertThat(addResponse.status).isEqualTo(HttpStatusCode.OK)

        val channels = getAllChannels()
        assertThat(channels).containsOnly(ChannelDto("Dummychannel"), ChannelDto("BatSign"))
    }

    @Test
    fun removing_channel_should_work() = runTest {
        val url = "http://localhost:8080/v3/channels/remove"
        val body = "{\"name\": \"Dummychannel\"}"

        val addResponse = client.request(url) {
            method = Post
            setBody(body)
        }

        assertThat(addResponse.status).isEqualTo(HttpStatusCode.OK)

        val channelsAfterDelete = getAllChannels()
        assertThat(channelsAfterDelete).isEmpty()
    }

    private suspend fun getAllChannels(): List<ChannelDto> {
        val url = "http://localhost:8080/v3/channels"

        val askResponse = client.request(url)

        assertThat(askResponse.status)
            .isEqualTo(HttpStatusCode.OK)

        return Json.decodeFromString(askResponse.bodyAsText())
    }
}
