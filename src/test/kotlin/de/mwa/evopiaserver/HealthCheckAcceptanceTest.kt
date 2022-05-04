package de.mwa.evopiaserver;

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class HealthCheckAcceptanceTest : ServerTestSetup() {

    @Test
    fun test_health_check() = testApplication {
        val client = HttpClient(CIO)
        val url = Url("http://0.0.0.0:8080/health")
        val response = client.request(url) {
            method = HttpMethod.Get
        }

        Assertions.assertThat((response.bodyAsText())).isEqualTo("Hello World!")

        client.close()
    }
}
