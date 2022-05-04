package de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.UserChannel
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UserChannelAcceptanceTest : ServerTestSetup() {
    @AfterEach
    fun localCleanup() {
        repositoryTestHelper.resetUserChannelTable()
        repositoryTestHelper.resetChannelTable()
    }

    @Test
    fun should_get_empty_user_channels_for_test_user() = runTest {
        val userChannels = allUserChannels()

        assertThat(userChannels).isEmpty()
    }

    @Test
    fun should_add_user_channel() = runTest {
        val body = "[{\"name\": \"Dummychannel\", \"value\":\"0160\"}]"
        addUserChannel(body)

        val userChannels = allUserChannels()
        assertThat(userChannels).containsOnly(UserChannel("Dummychannel", "0160"))
    }

    private suspend fun addUserChannel(body: String) {
        val addingUrl = "http://localhost:8080/v3/user/channel"
        val response = clientTestUser.request(addingUrl) {
            method = HttpMethod.Post
            setBody(body)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
    }

    @Test
    fun should_return_error_message_when_channel_not_existing() = runTest {
        val url = "http://localhost:8080/v3/user/channel"
        val body = "[{\"name\": \"NotExistingChannel\", \"value\":\"0160\"}]"

        val response = clientTestUser.request(url) {
            method = HttpMethod.Post
            setBody(body)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)

        val responseBody: String = response.body()
        assertThat(responseBody).contains("Unknown channel: NotExistingChannel")

        val userChannels = allUserChannels()
        assertThat(userChannels).isEmpty()
    }

    @Test
    fun should_remove_single_user_channel() = runTest {
        val body = "[{\"name\": \"Dummychannel\", \"value\":\"0160\"}]"
        addUserChannel(body)

        val emptyBody = "[]"
        val removingUrl = "http://localhost:8080/v3/user/channel"

        val response = clientTestUser.request(removingUrl) {
            method = HttpMethod.Post
            setBody(emptyBody)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)

        val userChannels = allUserChannels()
        assertThat(userChannels).isEmpty()
    }

    @Test
    fun should_remove_one_upsert_one_user_channels() = runTest {
        addChannel("NewChannel")
        val initialBody = "[{\"name\": \"Dummychannel\", \"value\":\"0160\"}," +
                "{\"name\": \"NewChannel\", \"value\":\"123456\"}]"
        addUserChannel(initialBody)

        val body = "[{\"name\": \"NewChannel\", \"value\":\"12346\"}]"
        val url = "http://localhost:8080/v3/user/channel"

        val response = clientTestUser.request(url) {
            method = HttpMethod.Post
            setBody(body)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)

        val userChannels = allUserChannels()
        assertThat(userChannels).containsOnly(UserChannel("NewChannel", "12346"))
    }

    @Test
    fun should_update_user_channel_value() = runTest {
        val body = "[{\"name\": \"Dummychannel\", \"value\":\"0160\"}]"
        addUserChannel(body)

        val bodyReplaced = "[{\"name\": \"Dummychannel\", \"value\":\"0161\"}]"
        addUserChannel(bodyReplaced)


        val userChannels = allUserChannels()
        assertThat(userChannels).containsOnly(UserChannel("Dummychannel", "0161"))
    }

    private suspend fun addChannel(name: String) {
        val url = "http://localhost:8080/v3/channels/add"
        val body = "{\"name\": \"$name\"}"

        val response = clientTestUser.request(url) {
            method = HttpMethod.Post
            setBody(body)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
    }

    private suspend fun allUserChannels(): List<UserChannel> {
        val url = "http://localhost:8080/v3/user/channel"

        val askResponse = clientTestUser.request(url)

        assertThat(askResponse.status).isEqualTo(HttpStatusCode.OK)

        return Json.decodeFromString(askResponse.bodyAsText())
    }
}
