package no.nav.familie.ef.søknad

import no.nav.security.mock.oauth2.MockOAuth2Server
import org.springframework.boot.builder.SpringApplicationBuilder

class ApplicationLocalLauncher : ApplicationLocalTestLauncher()

private val mockOauth2ServerPort: Int = 11588

fun main(args: Array<String>) {
    val mockOAuth2Server = MockOAuth2Server()
    mockOAuth2Server.start(mockOauth2ServerPort)
    Runtime.getRuntime().addShutdownHook(Thread { mockOAuth2Server.shutdown() })

    SpringApplicationBuilder(ApplicationLocalLauncher::class.java)
        .profiles(
            "local",
            "mock-kodeverk",
            "mock-dokument",
            "mock-pdl",
            "mock-pdlApp2AppClient",
            "mock-mottak",
            "mock-integrasjoner",
            "mock-saf",
            "mock-saksbehandling",
        ).run(*args)
}
