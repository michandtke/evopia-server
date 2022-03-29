package de.mwa.evopiaserver;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mwa.evopiaserver.api.dto.EventDto;
import de.mwa.evopiaserver.api.dto.TagDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureJson
@AutoConfigureJsonTesters
@ContextConfiguration(initializers = {EventsAcceptanceTest.Initializer.class})
public class EventsAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RepositoryTestHelper repositoryTestHelper;

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @AfterEach
    public void cleanup() {
        repositoryTestHelper.resetEventTagsTable();
        repositoryTestHelper.resetEventTable();
    }

    @Test
    public void should_get_events() {
        String events = getAllEventsString();

        assertThat(events).isEqualTo("[]");
    }

    @Test
    public void should_add_event_without_tag_and_get_it() throws JsonProcessingException {
        var body = "{" +
                "\"name\": \"nameIt\"," +
                "\"description\": \"desc\"," +
                "\"date\": \"2020\"," +
                "\"time\": \"18:00\"," +
                "\"place\": \"Berlin\"," +
                "\"imagePath\": \"img/path.jpg\"," +
                "\"tags\":[]" +
                "}";
        addEvent(body);

        List<EventDto> events = getAllEvents();
        assertThat(events).hasSize(1);
        var event = events.get(0);
        assertThat(event.getDate()).isEqualTo("2020");
        assertThat(event.getName()).isEqualTo("nameIt");
        assertThat(event.getDescription()).isEqualTo("desc");
        assertThat(event.getTime()).isEqualTo("18:00");
        assertThat(event.getPlace()).isEqualTo("Berlin");
        assertThat(event.getImagePath()).isEqualTo("img/path.jpg");
        assertThat(event.getTags()).isEmpty();
    }

    @Test
    public void should_add_event_with_tag_and_get_it() throws JsonProcessingException {
        var body = "{" +
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

        List<EventDto> events = getAllEvents();
        assertThat(events).hasSize(1);
        var event = events.get(0);
        assertThat(event.getDate()).isEqualTo("2020");
        assertThat(event.getName()).isEqualTo("nameIt");
        assertThat(event.getDescription()).isEqualTo("desc");
        assertThat(event.getTime()).isEqualTo("18:00");
        assertThat(event.getPlace()).isEqualTo("Berlin");
        assertThat(event.getImagePath()).isEqualTo("img/path.jpg");
        assertThat(event.getTags()).containsOnly(new TagDto("myTag"), new TagDto("mySecondTag"));
    }

    @Test
    public void should_delete_event() throws JsonProcessingException {
        var body = "{" +
                "\"name\": \"nameIt\"," +
                "\"description\": \"desc\"," +
                "\"date\": \"2020\"," +
                "\"time\": \"18:00\"," +
                "\"place\": \"Berlin\"," +
                "\"imagePath\": \"img/path.jpg\"," +
                "\"tags\":[]" +
                "}";
        addEvent(body);

        List<EventDto> eventBefore = getAllEvents();
        assertThat(eventBefore).hasSize(1);
        var id = eventBefore.get(0).component1();

        deleteEvent(id);

        List<EventDto> events = getAllEvents();
        assertThat(events).isEmpty();

    }

    private String getAllEventsString() {
        var askingUrl = "http://localhost:" + port + "/v2/events";
        var askResponse = restTemplate.exchange
                (askingUrl, HttpMethod.GET, HttpEntityFactory.forTestUser(), String.class);

        assertThat(askResponse.getStatusCode())
                .as("Not a successful call: " + askResponse.getBody())
                .isEqualTo(HttpStatus.OK);
        return askResponse.getBody();
    }

    private List<EventDto> getAllEvents() throws JsonProcessingException {
        var askingUrl = "http://localhost:" + port + "/v2/events";
        var askResponse = restTemplate.exchange
                (askingUrl, HttpMethod.GET, HttpEntityFactory.forTestUser(), String.class);

        assertThat(askResponse.getStatusCode())
                .as("Not a successful call: " + askResponse.getBody())
                .isEqualTo(HttpStatus.OK);

        ObjectMapper mapper = jacksonObjectMapper();
        var jsonInput = askResponse.getBody();
        return mapper.readValue(jsonInput, new TypeReference<List<EventDto>>(){});
    }

    private void addEvent(String body) {
        var addingUrl = "http://localhost:" + port + "/v2/events/upsert";
        var addResponse = restTemplate.exchange
                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(addResponse.getStatusCode())
                .as("Not a successful call: " + addResponse.getBody())
                .isEqualTo(HttpStatus.OK);
    }

    private void deleteEvent(Integer id) {
        var addingUrl = "http://localhost:" + port + "/v2/events/" + id;
        var removeResponse = restTemplate.exchange
                (addingUrl, HttpMethod.DELETE, HttpEntityFactory.forTestUser(), String.class);

        assertThat(removeResponse.getStatusCode())
                .as("Not a successful call: " + removeResponse.getBody())
                .isEqualTo(HttpStatus.OK);
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
