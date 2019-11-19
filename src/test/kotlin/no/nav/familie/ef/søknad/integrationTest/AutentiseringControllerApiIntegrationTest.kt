package no.nav.familie.ef.søknad.integrationTest


import no.nav.familie.ef.søknad.ApplicationLocalLauncher
import no.nav.security.token.support.core.JwtTokenConstants
import no.nav.security.token.support.test.JwtTokenGenerator
import org.glassfish.jersey.logging.LoggingFeature
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.Response
import kotlin.test.assertEquals

@ActiveProfiles("local")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [ApplicationLocalLauncher::class])
class AutentiseringControllerApiIntegrationTest {

    @Value("\${local.server.port}")
    val port: Int = 0
    val contextPath = "/api"
    val tokenSubject = "12345678911"

    @Test
    fun `skal få 200 når autentisert og vi bruker get`() {
        val response = webTarget().path("/innlogget")
                .request()
                .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer ${serializedJWTToken()}")
                .get()
        assertEquals(200, response.status)
    }

    @Test
    fun `skal få 401 når ikke autentisert `() {
        val response = webTarget().path("/innlogget")
                .request()
                .get()
        assertEquals(Response.Status.UNAUTHORIZED.statusCode, response.status)
    }

    private fun webTarget() = client().target("http://localhost:$port$contextPath")

    private fun client() = ClientBuilder.newClient().register(LoggingFeature::class.java)

    private fun serializedJWTToken() = JwtTokenGenerator.createSignedJWT(tokenSubject).serialize()

}

