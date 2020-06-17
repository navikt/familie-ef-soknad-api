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
import javax.ws.rs.client.Entity
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
        assertEquals(Response.Status.OK.statusCode, response.status)
    }

    @Test
    fun `skal få 401 når ikke autentisert `() {
        val response = webTarget().path("/innlogget")
                .request()
                .get()
        assertEquals(Response.Status.UNAUTHORIZED.statusCode, response.status)
    }

    @Test
    fun `skal få 404 når endepunkt ikke eksisterer`() {
        val response = webTarget().path("/eksistererIkke")
                .request()
                .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer ${serializedJWTToken()}")
                .get()
        assertEquals(Response.Status.NOT_FOUND.statusCode, response.status)
    }

    @Test
    fun `skal få 500 når man sender inn feil type objekt, liste i stedet for objekt`() {
        val response = webTarget().path("/soknad")
                .request()
                .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer ${serializedJWTToken()}")
                .post(Entity.json("[]"))
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.statusCode, response.status)
    }

    private fun webTarget() = client().target("http://localhost:$port$contextPath")

    private fun client() = ClientBuilder.newClient().register(LoggingFeature::class.java)

    private fun serializedJWTToken() = JwtTokenGenerator.createSignedJWT(tokenSubject).serialize()

}

