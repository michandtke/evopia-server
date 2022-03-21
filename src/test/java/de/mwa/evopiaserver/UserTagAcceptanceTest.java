package de.mwa.evopiaserver;

import de.mwa.evopiaserver.api.dto.UserTag;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.http.ResponseEntity;
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
@ContextConfiguration(initializers = {UserTagAcceptanceTest.Initializer.class})
public class UserTagAcceptanceTest {

    @Test
    public void should_get_user_for_test_user() {
        ResponseEntity<List<UserTag>> response = getUserTagsForTestUser();

        assertThat(response.getBody()).isEmpty();
    }

    @Test
    public void should_add_and_get_user_tag() {
        addTag("Beachvolleyball");
        var body = "[{\"name\": \"Beachvolleyball\"}]";
        var response = addUserChannel(body);

        assertThat(response).isEqualTo("Upserted user tags: 1 | Deleted user tags: 0");


        ResponseEntity<List<UserTag>> tags = getUserTagsForTestUser();
        assertThat(tags.getBody()).containsOnly(new UserTag("Beachvolleyball"));
    }

    @NotNull
    private ResponseEntity<List<UserTag>> getUserTagsForTestUser() {
        var url = "http://localhost:" + port + "/v2/user/tags";
        var requestType = new ParameterizedTypeReference<List<UserTag>>() {
        };
        var response = restTemplate.exchange
                (url, HttpMethod.GET, HttpEntityFactory.forTestUser(), requestType);

        assertThat(response.getStatusCode())
                .as("Not a successful call: " + response.getBody())
                .isEqualTo(HttpStatus.OK);
        return response;
    }

    private String addUserChannel(String body) {
        var addingUrl = "http://localhost:" + port + "/v2/user/tags";
        var addResponse = restTemplate.exchange
                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(addResponse.getStatusCode())
                .as("Not a successful call: " + addResponse.getBody())
                .isEqualTo(HttpStatus.OK);

        return addResponse.getBody();
    }

    private void addTag(String name) {
        var addingUrl = "http://localhost:" + port + "/v2/tags/add";
        var body = "{\"name\": \""+name+"\"}";
        var addResponse = restTemplate.exchange
                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @AfterEach
    public void cleanup() {
        repositoryTestHelper.resetUserTagTable();
    }

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
