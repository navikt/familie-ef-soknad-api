package no.nav.familie.ef.søknad

import no.nav.familie.ef.søknad.integrationTest.ApplicationLocalTestLauncher
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Import

@Import(ApplicationLocalTestLauncher::class)
@EnableMockOAuth2Server
class ApplicationLocalLauncher

fun main(args: Array<String>) {
    SpringApplicationBuilder(ApplicationLocalLauncher::class.java)
            .profiles(
                    "local",
                    "mock-kodeverk",
                    "mock-dokument",
                    "mock-pdl",
                    "mock-pdlApp2AppClient",
                    "mock-mottak",
                    "mock-integrasjoner"
            )
            .run(*args)
}
