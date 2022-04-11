package de.mwa.evopiaserver;

import de.mwa.evopiaserver.api.dto.ChannelDto;
import de.mwa.evopiaserver.api.dto.TagDto;
import org.junit.jupiter.api.AfterAll;
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
@ContextConfiguration(initializers = {TagAcceptanceTest.Initializer.class})
public class TagAcceptanceTest {
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

    @AfterAll
    static void endCleanup() {
        System.setProperty("spring.datasource.url", "");
        System.setProperty("spring.datasource.username", "");
        System.setProperty("spring.datasource.password", "");
    }

    @Test
    public void should_be_empty_initially() {
        List<TagDto> tags = allTags();

        assertThat(tags).isEmpty();
    }

    @Test
    public void should_add_tag() {
        var addingUrl = "http://localhost:" + port + "/v2/tags/add";
        var body = "[{\"name\": \"Some great thing\"}]";
        var addResponse = restTemplate.exchange
                (addingUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);

        assertThat(addResponse.getStatusCode())
                .as("Error: " + addResponse.getBody())
                .isEqualTo(HttpStatus.OK);

        List<TagDto> tags = allTags();
        assertThat(tags).containsOnly(new TagDto("Some great thing"));
    }

    private List<TagDto> allTags() {
        var askingUrl = "http://localhost:" + port + "/v2/tags";
        var requestType = new ParameterizedTypeReference<List<TagDto>>() {};
        var askResponse = restTemplate.exchange
                (askingUrl, HttpMethod.GET, HttpEntityFactory.forTestUser(), requestType);

        assertThat(askResponse.getStatusCode())
                .as("Error: " + askResponse.getBody())
                .isEqualTo(HttpStatus.OK);
        return askResponse.getBody();
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
            System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
            System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
        }
    }
}
