package de.mwa.evopiaserver;

import de.mwa.evopiaserver.dto.TagDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.server.testing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class TagAcceptanceTest : ServerTestSetup() {

    @AfterEach
    fun localCleanup() {
        repositoryTestHelper.resetUserChannelTable();
    }

    @Test
    fun should_be_empty_initially() = runTest {
        val tags = allTags()

        assertThat(tags).isEmpty()
    }

    @Test
    fun should_add_tag() = runTest {
        val url = "http://localhost:8080/v3/tags/add"
        val body = "[{\"name\": \"Some great thing\"}]"

        val response = client.request(url) {
            method = Post
            setBody(body)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)

        val tags = allTags()
        assertThat(tags).containsOnly(TagDto("Some great thing"))
    }

    private suspend fun allTags(): List<TagDto> {
        val url = "http://localhost:8080/v3/tags"

        val askResponse = client.request(url)

        assertThat(askResponse.status).isEqualTo(HttpStatusCode.OK)

        return Json.decodeFromString(askResponse.bodyAsText())
    }
}
