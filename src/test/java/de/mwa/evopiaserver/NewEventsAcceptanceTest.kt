package de.mwa.evopiaserver;


import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpGet
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClients
import de.mwa.evopiaserver.db.kotlin.DatabaseUtil
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.Config
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.DatabaseConfig
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.WebServer
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.jetty.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import liquibase.Contexts
import liquibase.Liquibase
import liquibase.database.DatabaseConnection
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import liquibase.resource.ResourceAccessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.jupiter.api.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI
import java.sql.DriverManager


@OptIn(ExperimentalCoroutinesApi::class)
@Testcontainers
class NewEventsAcceptanceTest {
    lateinit var server: NettyApplicationEngine

    @Container
    val postgreSQLContainer = PostgreSQLContainer("postgres")
        .withDatabaseName("integration-tests-db")
        .withUsername("sa")
        .withPassword("sa")

    @BeforeEach
    fun setup() {
        val databaseConfig = DatabaseConfig(databaseUrl)
        val config = Config(databaseConfig)
        val databaseUtil = DatabaseUtil(config)
        val env = applicationEngineEnvironment {
            module {
                WebServer.application(databaseUtil.database, this)
            }
            // Public API
            connector {
                host = "0.0.0.0"
                port = 8080
            }
        }
        server = embeddedServer(Netty, env).start(false)
    }

    private val databaseUrl by lazy {
        postgreSQLContainer.getJdbcUrl() + "&user=${postgreSQLContainer.username}&password=${postgreSQLContainer.password}"
    }

    @AfterEach
    fun teardown() {
        // clean up after this class, leave nothing dirty behind
        server.stop(1000, 10000)
    }

    val databaseUtil by lazy {
        val databaseConfig = DatabaseConfig(databaseUrl)
        val config = Config(databaseConfig)
        DatabaseUtil(config)
    }

    val repositoryTestHelper: RepositoryTestHelper by lazy {
        RepositoryTestHelper(databaseUtil)
    }

    @BeforeEach
    fun setUp() {
        val changeLogFile = "db.changelog-test.yaml"
        val liquibase: Liquibase = createLiquibase(changeLogFile)
        liquibase.update(Contexts())
        System.setProperty("JDBC_DATABASE_URL", databaseUrl);
    }

    private fun createLiquibase(changeLogFile: String): Liquibase {
        Class.forName("org.postgresql.Driver")
        val conn = DriverManager.getConnection(
            postgreSQLContainer.jdbcUrl,
            postgreSQLContainer.username,
            postgreSQLContainer.password
        )
        val dbConn: DatabaseConnection = JdbcConnection(conn)
        val resAcc: ResourceAccessor = ClassLoaderResourceAccessor()

        return Liquibase(changeLogFile, resAcc, dbConn)
    }

    @AfterEach
    fun cleanup() {
        repositoryTestHelper.resetEventTagsTable();
        repositoryTestHelper.resetEventTable();
        System.setProperty("JDBC_DATABASE_URL", "")
        System.setProperty("PORT", "")
    }

    @Test
    fun should_get_hello_world() = testApplication {
        val client = HttpClient(CIO)

        val url = Url("http://0.0.0.0:8080/health")

        val response = client.request(url) {
            method = HttpMethod.Get
        }
        assertThat((response.bodyAsText())).isEqualTo("Hello World!")
        assertThat(url).isNotNull
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
