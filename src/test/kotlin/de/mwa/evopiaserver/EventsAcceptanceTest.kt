package de.mwa.evopiaserver


import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.EventDto
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.dto.TagDto
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class EventsAcceptanceTest : ServerTestSetup() {

    @Test
    fun should_get_events() = runTest {
        val events = getAllEvents()

        assertThat(events).isEmpty()
    }


    @Test
    fun should_add_event_without_tag_and_get_it() = testApplication {
        val body = "{" +
                "\"name\": \"nameIt\"," +
                "\"description\": \"desc\"," +
                "\"from\": \"2022-02-27T18:00:00.000\"," +
                "\"to\": \"2022-02-27T19:00:00.000\"," +
                "\"place\": \"Berlin\"," +
                "\"imagePath\": \"img/path.jpg\"," +
                "\"tags\":[]" +
                "}"

        addEvent(body)

        val events = getAllEvents()

        assertThat(events).hasSize(1)
        val event: EventDto = events[0]
        assertThat(event.from).isEqualTo("2022-02-27T18:00:00.000")
        assertThat(event.name).isEqualTo("nameIt")
        assertThat(event.description).isEqualTo("desc")
        assertThat(event.to).isEqualTo("2022-02-27T19:00:00.000")
        assertThat(event.place).isEqualTo("Berlin")
        assertThat(event.imagePath).isEqualTo("img/path.jpg")
        assertThat(event.tags).isEmpty()
    }

    @Test
    fun should_add_event_with_tag_and_get_it() = testApplication {
        repositoryTestHelper.addTag("ExistingTag1")
        repositoryTestHelper.addTag("ExistingTag2")

        val body = "{" +
                "\"id\":-1," +
                "\"name\":\"5 - Beachen\"," +
                "\"description\":\"Beachen\"," +
                "\"from\":\"2022-02-27T18:00:00.000\"," +
                "\"to\":\"2022-02-27T19:00:00.000\"," +
                "\"place\":\"Beachmitte\"," +
                "\"imagePath\":\"assets/icons/ball-volleyball.svg\"," +
                "\"tags\":[" +
                "{\"name\":\"ExistingTag1\"}," +
                "{\"name\":\"ExistingTag2\"}" +
                "]}"
        addEvent(body)

        val events = getAllEvents()

        assertThat(events).hasSize(1)
        val event: EventDto = events[0]
        assertThat(event.from).isEqualTo("2022-02-27T18:00:00.000")
        assertThat(event.name).isEqualTo("5 - Beachen")
        assertThat(event.description).isEqualTo("Beachen")
        assertThat(event.to).isEqualTo("2022-02-27T19:00:00.000")
        assertThat(event.place).isEqualTo("Beachmitte")
        assertThat(event.imagePath).isEqualTo("assets/icons/ball-volleyball.svg")
        assertThat(event.tags).containsOnly(TagDto("ExistingTag1"), TagDto("ExistingTag2"))
    }

    @Test
    fun should_delete_event() = testApplication {
        val body = "{" +
                "\"name\": \"nameIt\"," +
                "\"description\": \"desc\"," +
                "\"from\": \"2022-02-27T18:00:00.000\"," +
                "\"to\": \"2022-02-27T19:00:00.000\"," +
                "\"place\": \"Berlin\"," +
                "\"imagePath\": \"img/path.jpg\"," +
                "\"tags\":[]" +
                "}"
        addEvent(body)

        val eventBefore = getAllEvents()
        assertThat(eventBefore).hasSize(1)
        val id = eventBefore[0].component1()

        deleteEvent(id)

        val eventAfter = getAllEvents()
        assertThat(eventAfter).isEmpty()

    }

    @Test
    fun should_only_update_one_event() = runTest {
        val body = "{" +
                "\"name\": \"nameIt\"," +
                "\"description\": \"desc\"," +
                "\"from\": \"2022-02-27T18:00:00.000\"," +
                "\"to\": \"2022-02-27T19:00:00.000\"," +
                "\"place\": \"Berlin\"," +
                "\"imagePath\": \"img/path.jpg\"," +
                "\"tags\":[]" +
                "}"

        addEvent(body)
        addEvent(body)

        val events = getAllEvents()

        val newPlace = "Heeresbaeckerei"
        val changedPlaceBody = "{" +
                "\"id\": ${events.first().id}" +
                "\"name\": \"nameIt\"," +
                "\"description\": \"desc\"," +
                "\"from\": \"2022-02-27T18:00:00.000\"," +
                "\"to\": \"2022-02-27T19:00:00.000\"," +
                "\"place\": \"$newPlace\"," +
                "\"imagePath\": \"img/path.jpg\"," +
                "\"tags\":[]" +
                "}"

        addEvent(changedPlaceBody)

        val eventsAfterChange = getAllEvents()
        val places = eventsAfterChange.map { it.place }
        assertThat(places).containsExactlyInAnyOrder("Berlin", newPlace)
        assertThat(eventsAfterChange.size).isEqualTo(events.size)
    }

    private suspend fun getAllEvents(): List<EventDto> {
        val url = "http://localhost:8080/v3/events"

        val client = HttpClient(CIO)
        val askResponse = client.request(url)

        assertThat(askResponse.status)
            .isEqualTo(HttpStatusCode.OK)

        client.close()

        return Json.decodeFromString(askResponse.bodyAsText())
    }

    private suspend fun addEvent(_body: String): HttpResponse {
        val client = HttpClient(CIO)
        val url = Url("http://localhost:8080/v3/events/upsert")
        val response = client.request(url) {
            method = HttpMethod.Post
            contentType(ContentType("application", "json"))
            setBody(_body)
        }
        val y = response.bodyAsText()
        assertThat(response.status)
            .`as`(response.bodyAsText())
            .isEqualTo(HttpStatusCode.OK)

        assertThat(y).isNotEmpty
        return response
    }

    private suspend fun deleteEvent(id: Int): HttpResponse {
        val client = HttpClient(CIO)
        val url = "http://localhost:8080/v3/events/$id"
        val response = client.request(url) {
            method = HttpMethod.Delete
        }
        assertThat(response.status)
            .isEqualTo(HttpStatusCode.OK)

        return response
    }
}
