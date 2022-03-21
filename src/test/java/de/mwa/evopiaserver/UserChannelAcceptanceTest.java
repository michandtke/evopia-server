package de.mwa.evopiaserver;

import de.mwa.evopiaserver.api.dto.UserChannel;
import org.assertj.core.api.Assertions;
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
@ContextConfiguration(initializers = {UserChannelAcceptanceTest.Initializer.class})
public class UserChannelAcceptanceTest {
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
        repositoryTestHelper.resetUserChannelTable();
    }

    @Test
    public void should_get_empty_user_channels_for_test_user() {
        List<UserChannel> userChannels = getAllUserChannels();

        Assertions.assertThat(userChannels).isEmpty();
    }

    @Test
    public void should_add_user_channel() {
        var body = "[{\"name\": \"Dummychannel\", \"value\":\"0160\"}]";
        addUserChannel(body);

        var userChannels = getAllUserChannels();
        assertThat(userChannels).containsOnly(new UserChannel("Dummychannel", "0160"));
    }

    private void addUserChannel(String body) {
        var addingUrl = "http://localhost:" + port + "/v2/user/channel";
        var addResponse = restTemplate.exchange
                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(addResponse.getStatusCode())
                .as("Not a successful call: " + addResponse.getBody())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    public void should_return_error_message_when_channel_not_existing() {
        var addingUrl = "http://localhost:" + port + "/v2/user/channel";
        var body = "[{\"name\": \"NotExistingChannel\", \"value\":\"0160\"}]";
        var addResponse = restTemplate.exchange
                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(addResponse.getStatusCode())
                .as("Should be a bad request: " + addResponse.getBody())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(addResponse.getBody()).contains("Unknown channel: NotExistingChannel");

        var userChannels = getAllUserChannels();
        assertThat(userChannels).isEmpty();
    }

    @Test
    public void should_remove_single_user_channel() {
        var body = "[{\"name\": \"Dummychannel\", \"value\":\"0160\"}]";
        addUserChannel(body);

        var emptyBody = "[]";
        var removingUrl = "http://localhost:" + port + "/v2/user/channel";
        var removeResponse = restTemplate.exchange
                (removingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(emptyBody), String.class);

        assertThat(removeResponse.getStatusCode())
                .as("Not a successful call: " + removeResponse.getBody())
                .isEqualTo(HttpStatus.OK);

        var userChannels = getAllUserChannels();
        assertThat(userChannels).isEmpty();
    }

    @Test
    public void should_remove_one_upsert_one_user_channels() {
        addChannel("NewChannel");
        var body = "[{\"name\": \"Dummychannel\", \"value\":\"0160\"}," +
                "{\"name\": \"NewChannel\", \"value\":\"123456\"}]";
        addUserChannel(body);

        var upsertingBody = "[{\"name\": \"NewChannel\", \"value\":\"12346\"}]";
        var upsertingUrl = "http://localhost:" + port + "/v2/user/channel";
        var upsertingResponse = restTemplate.exchange
                (upsertingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(upsertingBody), String.class);

        assertThat(upsertingResponse.getStatusCode())
                .as("Not a successful call: " + upsertingResponse.getBody())
                .isEqualTo(HttpStatus.OK);

        var userChannels = getAllUserChannels();
        assertThat(userChannels).containsOnly(new UserChannel("NewChannel", "12346"));
    }

    @Test
    public void should_update_user_channel_value() {
        var body = "[{\"name\": \"Dummychannel\", \"value\":\"0160\"}]";
        addUserChannel(body);

        var bodyReplaced = "[{\"name\": \"Dummychannel\", \"value\":\"0161\"}]";
        addUserChannel(bodyReplaced);


        var userChannels = getAllUserChannels();
        assertThat(userChannels).containsOnly(new UserChannel("Dummychannel", "0161"));
    }

    private void addChannel(String name) {
        var addingUrl = "http://localhost:" + port + "/v2/channels/add";
        var body = "{\"name\": \""+name+"\"}";
        var addResponse = restTemplate.exchange
                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private List<UserChannel> getAllUserChannels() {
        var askingUrl = "http://localhost:" + port + "/v2/user/channel";
        var requestType = new ParameterizedTypeReference<List<UserChannel>>() {
        };
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
