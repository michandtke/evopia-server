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
class NewEventsAcceptanceTest : ServerTestSetup() {

    @Test
    fun should_get_hello_world() = testApplication {
        val client = HttpClient(CIO)
        val url = Url("http://0.0.0.0:8080/health")
        val response = client.request(url) {
            method = HttpMethod.Get
        }

        assertThat((response.bodyAsText())).isEqualTo("Hello World!")

        client.close()
    }

    @Test
    fun should_get_events() = runTest {
        val events: String = getAllEventsString()

        assertThat(events).isEqualTo("[]")
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

        val allEvents = getAllEventsString()
        val events = Json.decodeFromString<List<EventDto>>(allEvents)

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

        val allEvents = getAllEventsString()
        val events = Json.decodeFromString<List<EventDto>>(allEvents)

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
//
//    @Test
//    public void should_delete_event() throws JsonProcessingException {
//        var body = "{" +
//                "\"name\": \"nameIt\"," +
//                "\"description\": \"desc\"," +
//                "\"date\": \"2020\"," +
//                "\"time\": \"18:00\"," +
//                "\"place\": \"Berlin\"," +
//                "\"imagePath\": \"img/path.jpg\"," +
//                "\"tags\":[]" +
//                "}";
//        addEvent(body);
//
//        List<EventDto> eventBefore = getAllEvents();
//        assertThat(eventBefore).hasSize(1);
//        var id = eventBefore.get(0).component1();
//
//        deleteEvent(id);
//
//        List<EventDto> events = getAllEvents();
//        assertThat(events).isEmpty();
//
//    }

    private suspend fun getAllEventsString(): String {
        val url = "http://localhost:8080/v3/events"

        val client = HttpClient(CIO)
        val askResponse = client.request(url)

        assertThat(askResponse.status)
            .isEqualTo(HttpStatusCode.OK);

        client.close()

        return askResponse.bodyAsText()
    }

    //    private List<EventDto> getAllEvents() throws JsonProcessingException {
//        var askingUrl = "http://localhost:" + port + "/v3/events";
//        var askResponse = restTemplate.exchange
//                (askingUrl, HttpMethod.GET, HttpEntityFactory.forTestUser(), String.class);
//
//        assertThat(askResponse.getStatusCode())
//                .as("Not a successful call: " + askResponse.getBody())
//                .isEqualTo(HttpStatus.OK);
//
//        ObjectMapper mapper = jacksonObjectMapper();
//        var jsonInput = askResponse.getBody();
//        return mapper.readValue(jsonInput, new TypeReference<List<EventDto>>(){});
//    }
//
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

//    private void deleteEvent(Integer id) {
//        var addingUrl = "http://localhost:" + port + "/v3/events/" + id;
//        var removeResponse = restTemplate.exchange
//                (addingUrl, HttpMethod.DELETE, HttpEntityFactory.forTestUser(), String.class);
//
//        assertThat(removeResponse.getStatusCode())
//                .as("Not a successful call: " + removeResponse.getBody())
//                .isEqualTo(HttpStatus.OK);
//    }
}
