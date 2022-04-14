package de.mwa.evopiaserver;


import de.mwa.evopiaserver.api.dto.EventDto
import de.mwa.evopiaserver.api.dto.TagDto
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
                "\"date\": \"2020\"," +
                "\"time\": \"18:00\"," +
                "\"place\": \"Berlin\"," +
                "\"imagePath\": \"img/path.jpg\"," +
                "\"tags\":[]" +
                "}"

        addEvent(body)

        val events = getAllEvents()

        assertThat(events).hasSize(1)
        val event: EventDto = events[0]
        assertThat(event.date).isEqualTo("2020")
        assertThat(event.name).isEqualTo("nameIt")
        assertThat(event.description).isEqualTo("desc")
        assertThat(event.time).isEqualTo("18:00")
        assertThat(event.place).isEqualTo("Berlin")
        assertThat(event.imagePath).isEqualTo("img/path.jpg")
        assertThat(event.tags).isEmpty()
    }

    @Test
    fun should_add_event_with_tag_and_get_it() = testApplication {
        val body = "{" +
                "\"name\": \"nameIt\"," +
                "\"description\": \"desc\"," +
                "\"date\": \"2020\"," +
                "\"time\": \"18:00\"," +
                "\"place\": \"Berlin\"," +
                "\"imagePath\": \"img/path.jpg\"," +
                "\"tags\":[ {\"name\":\"myTag\"}," +
                "{\"name\":\"mySecondTag\"}]" +
                "}";
        addEvent(body);

        val events = getAllEvents()

        assertThat(events).hasSize(1)
        val event: EventDto = events[0]
        assertThat(event.date).isEqualTo("2020");
        assertThat(event.name).isEqualTo("nameIt");
        assertThat(event.description).isEqualTo("desc");
        assertThat(event.time).isEqualTo("18:00");
        assertThat(event.place).isEqualTo("Berlin");
        assertThat(event.imagePath).isEqualTo("img/path.jpg");
        assertThat(event.tags).containsOnly(TagDto("myTag"), TagDto("mySecondTag"));
    }

    @Test
    fun should_delete_event() = testApplication {
        val body = "{" +
                "\"name\": \"nameIt\"," +
                "\"description\": \"desc\"," +
                "\"date\": \"2020\"," +
                "\"time\": \"18:00\"," +
                "\"place\": \"Berlin\"," +
                "\"imagePath\": \"img/path.jpg\"," +
                "\"tags\":[]" +
                "}";
        addEvent(body);

        val eventBefore = getAllEvents()
        assertThat(eventBefore).hasSize(1)
        val id = eventBefore[0].component1()

        deleteEvent(id)

        val eventAfter = getAllEvents()
        assertThat(eventAfter).isEmpty()

    }

    private suspend fun getAllEvents(): List<EventDto> {
        val url = "http://localhost:8080/v3/events"

        val client = HttpClient(CIO)
        val askResponse = client.request(url)

        assertThat(askResponse.status)
            .isEqualTo(HttpStatusCode.OK);

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
