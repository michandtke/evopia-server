package de.mwa.evopiaserver.profile;

import com.jayway.jsonpath.JsonPath;
import org.apache.tomcat.util.codec.binary.Base64;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureJson
@AutoConfigureJsonTesters
@ContextConfiguration(initializers = {LoadProfileIntegrationTest.Initializer.class})
public class LoadProfileIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    @Test
    public void shouldBeAlive() {
        var url = "http://localhost:" + port + "/health";
        var response = restTemplate.exchange
                (url, HttpMethod.GET, new HttpEntity(createHeaders("test@test.com", "test")), String.class);

        assertThat(response.getBody()).isNotBlank();
        assertThat(response.getBody()).contains("alive!");
    }

    @Test
    @Transactional
    public void shouldGetEmptyProfileFromTestUser() {
        var url = "http://localhost:" + port + "/v2/profile";
        var response = restTemplate.exchange
                (url, HttpMethod.GET, new HttpEntity(createHeaders("test@test.com", "test")), String.class);

        assertThat(response.getBody()).isNotBlank();
        System.out.println(response.getBody());

        var parsedJson = JsonPath.parse(response.getBody());
        String imagePath = parsedJson.read("@.imagePath");
        assertThat(imagePath).isBlank();
        List channels = parsedJson.read("@.profileChannels");
        assertThat(channels).isEmpty();
        List tags = parsedJson.read("@.tags");
        assertThat(tags).isEmpty();
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
