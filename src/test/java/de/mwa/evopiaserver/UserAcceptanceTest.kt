package de.mwa.evopiaserver

import de.mwa.evopiaserver.registration.User
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.*


@OptIn(ExperimentalCoroutinesApi::class)
class UserAcceptanceTest : ServerTestSetup() {

    @AfterEach
    fun localCleanup() {
        repositoryTestHelper.resetUserTable()
    }

    @Test
    fun should_get_user_for_test_user() = runTest {
        val user = getTestUser()

        assertThat(user.firstName).isEqualTo("Bruce")
        assertThat(user.imagePath).isBlank()
    }


    private suspend fun getTestUser(): User {
        return getUserWith(clientTestUser)
    }

    private suspend fun getUserWith(client: HttpClient): User {
        val url = "http://localhost:8080/v3/user"

        val response = client.request(url) {
            method = HttpMethod.Get
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)

        return Json.decodeFromString(response.bodyAsText())
    }

    @Test
    fun should_be_able_to_add_and_use_new_user() = runTest {
        val url = "http://localhost:8080/v3/user"
        val dateOfRegistration = Date().toString()
        val email = "Kanzler@wichtig.de"
        val body = "{" +
                "\"firstName\": \"Olaf\"," +
                "\"lastName\": \"Scholz\"," +
                "\"dateOfRegistration\": \"" + dateOfRegistration + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"123pw\"," +
                "\"imagePath\": \"Olaf.jpg\"" +
                "}"

        val response = client.request(url) {
            method = HttpMethod.Put
            setBody(body)
        }
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)

        val clientNewUser =  HttpClient(CIO) {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(username = email, password = "123pw")
                    }
                }
            }
        }
        val newUser = getUserWith(clientNewUser)

        assertThat(newUser.dateOfRegistration).isEqualTo(dateOfRegistration)
    }

    @Test
    fun should_be_able_to_upsert_user_image_path() = runTest {
        val url = "http://localhost:8080/v3/user"
        val newImagePath = "NewImageOlaf.jpg"
        val body = "{" +
                "\"imagePath\": \"" + newImagePath + "\"" +
                "}"

        val response = clientTestUser.request(url) {
            method = HttpMethod.Post
            setBody(body)
        }
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)

        val adjustedUser = getTestUser()

        assertThat(adjustedUser.imagePath).isEqualTo(newImagePath)
    }
}
