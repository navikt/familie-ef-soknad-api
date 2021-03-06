package no.nav.familie.ef.søknad.api

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.ApplicationLocalLauncher
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.kontrakter.ef.søknad.SøknadType
import no.nav.familie.kontrakter.ef.søknad.dokumentasjonsbehov.DokumentasjonsbehovDto
import no.nav.security.token.support.core.JwtTokenConstants
import no.nav.security.token.support.test.JwtTokenGenerator
import org.assertj.core.api.Assertions.assertThat
import org.glassfish.jersey.logging.LoggingFeature
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.util.*
import javax.ws.rs.client.ClientBuilder

@Profile("dokumentasjonsbehov-test")
@Configuration
class DokumentasjonsbehovControllerTestConfiguration {

    @Primary
    @Bean
    fun søknadClient(): SøknadClient = mockk()
}

@ActiveProfiles("local", "dokumentasjonsbehov-test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [ApplicationLocalLauncher::class])
internal class DokumentasjonsbehovControllerTest {

    @Value("\${local.server.port}")
    val port: Int = 0
    val contextPath = "/api"
    val tokenSubject = "12345678911"

    @Autowired lateinit var søknadClient: SøknadClient

    @Test
    internal fun `fnr i token er lik fnr i søknaden`() {
        every { søknadClient.hentDokumentasjonsbehovForSøknad(any()) } returns lagDokumentasjonsbehov(tokenSubject)

        val response = webTarget().path("/dokumentasjonsbehov/${UUID.randomUUID()}")
                .request()
                .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer ${serializedJWTToken()}")
                .get()

        assertThat(response.status).isEqualTo(200)
    }

    @Test
    internal fun `fnr i token er ikke lik fnr i søknaden`() {
        every { søknadClient.hentDokumentasjonsbehovForSøknad(any()) } returns lagDokumentasjonsbehov("0")

        val response = webTarget().path("/dokumentasjonsbehov/${UUID.randomUUID()}")
                .request()
                .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer ${serializedJWTToken()}")
                .get()

        assertThat(response.status).isEqualTo(403)
    }

    private fun lagDokumentasjonsbehov(fødselsnummer: String): DokumentasjonsbehovDto =
            DokumentasjonsbehovDto(emptyList(),
                                   LocalDateTime.now(),
                                   SøknadType.OVERGANGSSTØNAD,
                                   fødselsnummer)

    private fun webTarget() = client().target("http://localhost:$port$contextPath")

    private fun client() = ClientBuilder.newClient().register(LoggingFeature::class.java)

    private fun serializedJWTToken() = JwtTokenGenerator.createSignedJWT(tokenSubject).serialize()
}