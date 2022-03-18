package de.mwa.evopiaserver.profile;

import com.jayway.jsonpath.JsonPath;
import de.mwa.evopiaserver.HttpEntityFactory;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureJson
@AutoConfigureJsonTesters
@ContextConfiguration(initializers = {UserIntegrationTest.Initializer.class})
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Test
    public void should_get_user_for_test_user() {
        ResponseEntity<String> response = getUserWith(HttpEntityFactory.forTestUser());

        var parsedJson = JsonPath.parse(response.getBody());
        String imagePath = parsedJson.read("@.imagePath");
        assertThat(imagePath).isBlank();
    }

    @NotNull
    private ResponseEntity<String> getUserWith(HttpEntity<String> entity) {
        var url = "http://localhost:" + port + "/v2/user";
        var response = restTemplate.exchange
                (url, HttpMethod.GET, entity, String.class);

        assertThat(response.getStatusCode())
                .as("Not a successful call: " + response.getBody())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();
        System.out.println(response.getBody());
        return response;
    }

    @Test
    public void should_be_able_to_add_and_use_new_user() {
        var url = "http://localhost:" + port + "/v2/user";
        var dateOfRegistration = new Date().toString();
        var email = "Kanzler@wichtig.de";
        var body = "{" +
                "\"firstName\": \"Olaf\"," +
                "\"lastName\": \"Scholz\"," +
                "\"dateOfRegistration\": \"" + dateOfRegistration + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"123pw\"," +
                "\"imagePath\": \"Olaf.jpg\"" +
                "}";

        var response = restTemplate.exchange
                (url, HttpMethod.PUT, HttpEntityFactory.forTestUserWith(body), String.class);
        assertThat(response.getStatusCode())
                .as("Not a successful call: " + response.getBody())
                .isEqualTo(HttpStatus.OK);

        ResponseEntity<String> newUser = getUserWith(HttpEntityFactory.forUser(email, "123pw"));

        var parsedJson = JsonPath.parse(newUser.getBody());
        String readDate = parsedJson.read("@.dateOfRegistration");
        assertThat(readDate).isEqualTo(dateOfRegistration);
    }

    @Test
    public void should_be_able_to_upsert_user_image_path() {
        var url = "http://localhost:" + port + "/v2/user";
        var newImagePath = "NewImageOlaf.jpg";
        var body = "{" +
                "\"imagePath\": \"" + newImagePath + "\"" +
                "}";

        var response = restTemplate.exchange
                (url, HttpMethod.POST, HttpEntityFactory.forTestUserWith(body), String.class);
        assertThat(response.getStatusCode())
                .as("Not a successful call: " + response.getBody())
                .isEqualTo(HttpStatus.OK);

        ResponseEntity<String> adjustedUser = getUserWith(HttpEntityFactory.forTestUser());

        var parsedJson = JsonPath.parse(adjustedUser.getBody());
        String imagePath = parsedJson.read("@.imagePath");
        assertThat(imagePath).isEqualTo(newImagePath);
    }

//    @Test
//    public void should_save_image_path_tags_and_channels_in_profile() {
//        var saveUrl = "http://localhost:" + port + "/v2/profile/save";
//        var saveBody = "{\"imagePath\":\"myImagePath\"," +
//                "\"profileChannels\": [ {\"name\":\"Dummychannel\", \"value\":\"1234\"} ]," +
//                "\"tags\": [ {\"name\":\"Dummytag\"}]" +
//                "}";
//        var saveResponse = restTemplate.exchange
//                (saveUrl, HttpMethod.POST, HttpEntityFactory.forTestUserWith(saveBody), String.class);
//
//        assertThat(saveResponse.getStatusCode())
//                .as("Not a successful call: " + saveResponse.getBody())
//                .isEqualTo(HttpStatus.OK);
//
//        var url = "http://localhost:" + port + "/v2/profile";
//        var response = restTemplate.exchange
//                (url, HttpMethod.GET, HttpEntityFactory.forTestUser(), String.class);
//
//        assertThat(response.getStatusCode())
//                .as("Not a successful call: " + response.getBody())
//                .isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotBlank();
//        System.out.println(response.getBody());
//
//        var parsedJson = JsonPath.parse(response.getBody());
//        String imagePath = parsedJson.read("@.imagePath");
//        assertThat(imagePath).isEqualTo("myImagePath");
//
//        List channels = parsedJson.read("@.profileChannels");
//        UserChannel expectedChannel = UserChannel.builder()
//                .name("Dummychannel")
//                .value("12345")
//                .build();
//        assertThat(channels).containsOnly(expectedChannel);
//
//        List tags = parsedJson.read("@.tags");
//        UserTag expectedTag = UserTag.builder().name("Dummytag").build();
//        assertThat(tags).containsOnly(expectedTag);
//    }

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
