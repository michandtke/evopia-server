package de.mwa.evopiaserver;


import de.mwa.evopiaserver.api.dto.ChannelDto;
import de.mwa.evopiaserver.api.dto.EventDto;
import de.mwa.evopiaserver.profile.UserIntegrationTest;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

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
        repositoryTestHelper.resetEventTable();
    }

    @Test
    public void should_get_events() {
        String events = getAllEventsString();

        assertThat(events).isEqualTo("[]");
    }

    @Test
    public void should_add_event_and_get_it() {
        var body = "{" +
                "\"name\": \"nameIt\"," +
                "\"description\": \"desc\"," +
                "\"date\": \"2020\"," +
                "\"time\": \"18:00\"," +
                "\"place\": \"Berlin\"," +
                "\"imagePath\": \"img/path.jpg\"" +
                "}";
        addEvent(body);

        List<EventDto> events = getAllEvents();
        assertThat(events).containsOnly(
                new EventDto(1, "nameIt", "desc", "2020", "18:00", "Berlin", "img/path.jpg")
        );
    }

    @Test
    public void should_delete_event() {

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

    private List<EventDto> getAllEvents() {
        var askingUrl = "http://localhost:" + port + "/v2/events";
        var requestType = new ParameterizedTypeReference<List<EventDto>>() {
        };
        var askResponse = restTemplate.exchange
                (askingUrl, HttpMethod.GET, HttpEntityFactory.forTestUser(), requestType);

        assertThat(askResponse.getStatusCode())
                .as("Not a successful call: " + askResponse.getBody())
                .isEqualTo(HttpStatus.OK);
        return askResponse.getBody();
    }

    private void addEvent(String body) {
        var addingUrl = "http://localhost:" + port + "/v2/events/upsert";
        var addResponse = restTemplate.exchange
                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(addResponse.getStatusCode())
                .as("Not a successful call: " + addResponse.getBody())
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
