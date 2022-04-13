package de.mwa.evopiaserver

import de.mwa.evopiaserver.db.kotlin.DatabaseUtil
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.Config
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.DatabaseConfig
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.WebServer
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import liquibase.Contexts
import liquibase.Liquibase
import liquibase.database.DatabaseConnection
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import liquibase.resource.ResourceAccessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.DriverManager

@Testcontainers
open class ServerTestSetup {
    lateinit var server: NettyApplicationEngine
    var initialized = false;

    @Container
    val postgreSQLContainer = PostgreSQLContainer("postgres")
        .withDatabaseName("integration-tests-db")
        .withUsername("sa")
        .withPassword("sa")

    @BeforeEach
    fun setup() {
        if (initialized) { return }
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

    @BeforeEach
    fun setUp() {
        if (initialized) { return }
        val changeLogFile = "db.changelog-test.yaml"
        val liquibase: Liquibase = createLiquibase(changeLogFile)
        liquibase.update(Contexts())
//        System.setProperty("JDBC_DATABASE_URL", databaseUrl);
    }

    @AfterEach
    fun cleanup() {
        repositoryTestHelper.resetEventTagsTable();
        repositoryTestHelper.resetEventTable();
//        System.setProperty("JDBC_DATABASE_URL", "")
//        System.setProperty("PORT", "")
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
}