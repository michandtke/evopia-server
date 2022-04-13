package de.mwa.evopiaserver;


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.jetty.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    }

    @Test
    fun should_get_events() = runTest {
        val events: String = getAllEventsString()

        assertThat(events).isEqualTo("[]")
    }

//
//    @Test
//    public void should_add_event_without_tag_and_get_it() throws JsonProcessingException {
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
//        List<EventDto> events = getAllEvents();
//        assertThat(events).hasSize(1);
//        var event = events.get(0);
//        assertThat(event.getDate()).isEqualTo("2020");
//        assertThat(event.getName()).isEqualTo("nameIt");
//        assertThat(event.getDescription()).isEqualTo("desc");
//        assertThat(event.getTime()).isEqualTo("18:00");
//        assertThat(event.getPlace()).isEqualTo("Berlin");
//        assertThat(event.getImagePath()).isEqualTo("img/path.jpg");
//        assertThat(event.getTags()).isEmpty();
//    }
//
//    @Test
//    public void should_add_event_with_tag_and_get_it() throws JsonProcessingException {
//        var body = "{" +
//                "\"name\": \"nameIt\"," +
//                "\"description\": \"desc\"," +
//                "\"date\": \"2020\"," +
//                "\"time\": \"18:00\"," +
//                "\"place\": \"Berlin\"," +
//                "\"imagePath\": \"img/path.jpg\"," +
//                "\"tags\":[ {\"name\":\"myTag\"}," +
//                "{\"name\":\"mySecondTag\"}]" +
//                "}";
//        addEvent(body);
//
//        List<EventDto> events = getAllEvents();
//        assertThat(events).hasSize(1);
//        var event = events.get(0);
//        assertThat(event.getDate()).isEqualTo("2020");
//        assertThat(event.getName()).isEqualTo("nameIt");
//        assertThat(event.getDescription()).isEqualTo("desc");
//        assertThat(event.getTime()).isEqualTo("18:00");
//        assertThat(event.getPlace()).isEqualTo("Berlin");
//        assertThat(event.getImagePath()).isEqualTo("img/path.jpg");
//        assertThat(event.getTags()).containsOnly(new TagDto("myTag"), new TagDto("mySecondTag"));
//    }
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
        val askingUrl = "http://localhost:8080/v3/events"
//        val askResponse: HttpResponse = HttpClient(CIO).use { client ->
//            async { client.request(askingUrl) }
//        }
//
//        val req1 = async { client.call("https://127.0.0.1:8080/a").response.readBytes() }
////        var askResponse = restTemplate.exchange
////                (askingUrl, HttpMethod.GET, HttpEntityFactory.forTestUser(), String.class);
//
//        assertThat(askResponse.status)
//                .isEqualTo(HttpStatusCode.OK);
//        return runBlocking { askResponse.bodyAsText() }
        return getResponse(askingUrl)
    }

    private suspend fun getResponse(url: String): String {
        val client = HttpClient(Jetty)
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
//    private void addEvent(String body) {
//        var addingUrl = "http://localhost:" + port + "/v3/events/upsert";
//        var addResponse = restTemplate.exchange
//                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);
//
//        assertThat(addResponse.getStatusCode())
//                .as("Not a successful call: " + addResponse.getBody())
//                .isEqualTo(HttpStatus.OK);
//    }
//
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
