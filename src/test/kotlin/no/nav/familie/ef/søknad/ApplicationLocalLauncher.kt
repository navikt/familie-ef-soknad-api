package no.nav.familie.ef.søknad

import no.nav.familie.ef.søknad.integrationTest.ApplicationLocalTestLauncher
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.springframework.boot.builder.SpringApplicationBuilder

@EnableMockOAuth2Server
class ApplicationLocalLauncher : ApplicationLocalTestLauncher()

/**
 * Denne settes til en fixed port for å kunne bruke samme port som familie-dokument
 */
private val mockOauth2ServerPort: String = "11588"

fun main(args: Array<String>) {
    SpringApplicationBuilder(ApplicationLocalLauncher::class.java)
        .profiles(
            "local",
            "mock-kodeverk",
            "mock-dokument",
            "mock-pdl",
            "mock-pdlApp2AppClient",
            "mock-mottak",
            "mock-integrasjoner",
        )
        .properties(mapOf("mock-oauth2-server.port" to mockOauth2ServerPort))
        .run(*args)
}
