package no.nav.familie.ef.søknad.integrationTest

import no.nav.familie.ef.søknad.ApplicationLocalLauncher
import no.nav.security.token.support.core.JwtTokenConstants
import no.nav.security.token.support.test.JwtTokenGenerator
import org.assertj.core.api.Assertions.assertThat
import org.glassfish.jersey.logging.LoggingFeature
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.ws.rs.client.ClientBuilder

@ActiveProfiles("local", "mock-pdl", "mock-pdlApp2AppClient")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [ApplicationLocalLauncher::class])
class OppslagControllerIntegrationTest {

    @Value("\${local.server.port}")
    val port: Int = 0
    val contextPath = "/api/oppslag"
    val tokenSubject = "12345678911"

    @Test
    fun `Få response uten _navn fra sokerinfo endepunkt - skal feile hvis noen fjerner private fra _navn`() {
        val response = webTarget().path("/sokerinfo")
            .request()
            .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer ${serializedJWTToken()}")
            .get()
        val readEntity = response.readEntity(String::class.java)
        assertThat(readEntity).doesNotContain("_navn")
    }

    private fun webTarget() = client().target("http://localhost:$port$contextPath")

    private fun client() = ClientBuilder.newClient().register(LoggingFeature::class.java)

    private fun serializedJWTToken() = JwtTokenGenerator.createSignedJWT(tokenSubject).serialize()
}
