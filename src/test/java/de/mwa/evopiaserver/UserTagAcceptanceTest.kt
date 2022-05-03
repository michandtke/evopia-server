package de.mwa.evopiaserver

import de.mwa.evopiaserver.api.dto.UserTag
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class UserTagAcceptanceTest : ServerTestSetup() {

    @AfterEach
    fun localCleanup() {
        repositoryTestHelper.resetUserTagTable()
        repositoryTestHelper.resetTagTable()
    }

    @Test
    fun should_get_user_for_test_user() = runTest {
        val response = userTagsForTestUser()
        Assertions.assertThat(response).isEmpty()
    }

    @Test
    fun should_add_and_get_user_tag() = runTest {
        addTag("Beachvolleyball")
        val body = "[{\"name\": \"Beachvolleyball\"}]"
        val response = addUserChannel(body)
        Assertions.assertThat(response).isEqualTo("Upserted user tags: 1 | Deleted user tags: 0")
        val tags = userTagsForTestUser()
        Assertions.assertThat(tags).containsOnly(UserTag("Beachvolleyball"))
    }

    private suspend fun userTagsForTestUser(): List<UserTag> {
        val url = "http://localhost:8080/v3/user/tags"

        val askResponse = clientTestUser.request(url)

        Assertions.assertThat(askResponse.status).isEqualTo(HttpStatusCode.OK)

        return Json.decodeFromString(askResponse.bodyAsText())
    }

    private suspend fun addUserChannel(body: String): String {
        val addingUrl = "http://localhost:8080/v3/user/tags"
        val response = clientTestUser.request(addingUrl) {
            method = HttpMethod.Post
            setBody(body)
        }
        Assertions.assertThat(response.status)
            .isEqualTo(HttpStatusCode.OK)
        return response.body()
    }

    private suspend fun addTag(name: String) {
        val addingUrl = "http://localhost:8080/v3/tags/add"
        val body = "[{\"name\": \"$name\"}]"
        val response = clientTestUser.request(addingUrl) {
            method = HttpMethod.Post
            setBody(body)
        }
        Assertions.assertThat(response.status)
            .isEqualTo(HttpStatusCode.OK)
    }
}