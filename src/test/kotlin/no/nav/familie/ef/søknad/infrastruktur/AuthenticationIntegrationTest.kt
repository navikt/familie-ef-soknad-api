package no.nav.familie.ef.søknad.infrastruktur

import no.nav.familie.ef.søknad.infrastruktur.sikkerhet.AuthResponse
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.exchange
import java.util.UUID

class AuthenticationIntegrationTest : OppslagSpringRunnerTest() {
    private val beskyttetEndepunkt get() = localhost("/api/innlogget")

    @Test
    fun `skal få 200 når gyldig token er oppgitt`() {
        val response =
            restTemplate.exchange<AuthResponse>(
                beskyttetEndepunkt,
                HttpMethod.GET,
                HttpEntity<Any>(HttpHeaders().apply { setBearerAuth(søkerBearerToken()) }),
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(AuthResponse("Autentisert kall"))
    }

    @Test
    fun `skal få 401 når ingen token er oppgitt`() {
        val exception =
            assertThrows<HttpClientErrorException.Unauthorized> {
                restTemplate.exchange<String>(
                    beskyttetEndepunkt,
                    HttpMethod.GET,
                    HttpEntity<Any>(HttpHeaders()),
                )
            }

        assertThat(exception.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `skal få 401 når token har for lavt sikkerhetsnivå (acr Level3)`() {
        val tokenMedLavtSikkerhetsnivå =
            mockOAuth2Server
                .issueToken(
                    "tokenx",
                    UUID.randomUUID().toString(),
                    DefaultOAuth2TokenCallback(
                        issuerId = "tokenx",
                        subject = "12345678901",
                        audience = listOf("familie-app"),
                        claims = mapOf("acr" to "Level3"),
                        expiry = 3600,
                    ),
                ).serialize()

        val exception =
            assertThrows<HttpClientErrorException.Unauthorized> {
                restTemplate.exchange<String>(
                    beskyttetEndepunkt,
                    HttpMethod.GET,
                    HttpEntity<Any>(HttpHeaders().apply { setBearerAuth(tokenMedLavtSikkerhetsnivå) }),
                )
            }

        assertThat(exception.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `skal få 401 når token har feil audience`() {
        val tokenMedFeilAudience =
            mockOAuth2Server
                .issueToken(
                    "tokenx",
                    UUID.randomUUID().toString(),
                    DefaultOAuth2TokenCallback(
                        issuerId = "tokenx",
                        subject = "12345678901",
                        audience = listOf("annen-app"),
                        claims = mapOf("acr" to "Level4"),
                        expiry = 3600,
                    ),
                ).serialize()

        val exception =
            assertThrows<HttpClientErrorException.Unauthorized> {
                restTemplate.exchange<String>(
                    beskyttetEndepunkt,
                    HttpMethod.GET,
                    HttpEntity<Any>(HttpHeaders().apply { setBearerAuth(tokenMedFeilAudience) }),
                )
            }

        assertThat(exception.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}
