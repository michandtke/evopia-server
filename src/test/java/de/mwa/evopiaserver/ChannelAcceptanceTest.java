package de.mwa.evopiaserver;

import de.mwa.evopiaserver.api.dto.ChannelDto;
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
@ContextConfiguration(initializers = {ChannelAcceptanceTest.Initializer.class})
public class ChannelAcceptanceTest {
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
        repositoryTestHelper.resetChannelTable();
    }

    @Test
    public void initial_channel_has_only_dummychannel() {
        List<ChannelDto> channels = getAllChannels();

        assertThat(channels).containsOnly(new ChannelDto("Dummychannel"));
    }

    @Test
    public void adding_removing_channel_should_work() {
        var addingUrl = "http://localhost:" + port + "/v2/channels/add";
        var body = "{\"name\": \"BatSign\"}";
        var addResponse = restTemplate.exchange
                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        var channels = getAllChannels();
        assertThat(channels).containsOnly(new ChannelDto("Dummychannel"), new ChannelDto("BatSign"));
    }

    @Test
    public void removing_channel_should_work() {
        var removingUrl = "http://localhost:" + port + "/v2/channels/remove";
        var body = "{\"name\": \"Dummychannel\"}";
        var removeResponse = restTemplate.exchange
                (removingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(removeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        var channelsAfterDelete = getAllChannels();
        assertThat(channelsAfterDelete).isEmpty();
    }

    private List<ChannelDto> getAllChannels() {
        var askingUrl = "http://localhost:" + port + "/v2/channels";
        var requestType = new ParameterizedTypeReference<List<ChannelDto>>() {};
        var askResponse = restTemplate.exchange
                (askingUrl, HttpMethod.GET, HttpEntityFactory.forTestUser(), requestType);

        assertThat(askResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        return askResponse.getBody();
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
