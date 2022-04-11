package de.mwa.evopiaserver;

import org.junit.jupiter.api.AfterAll;
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
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureJson
@AutoConfigureJsonTesters
@ContextConfiguration(initializers = {HealthCheckAcceptanceTest.Initializer.class})
public class HealthCheckAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @AfterAll
    static void endCleanup() {
        System.setProperty("spring.datasource.url", "");
        System.setProperty("spring.datasource.username", "");
        System.setProperty("spring.datasource.password", "");
    }

    @Test
    public void shouldBeAlive() {
        var url = "http://localhost:" + port + "/health";
        var response = restTemplate.exchange
                (url, HttpMethod.GET, HttpEntityFactory.forTestUser(), String.class);

        assertThat(response.getBody()).isNotBlank();
        assertThat(response.getBody()).contains("alive!");
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
