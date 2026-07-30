package no.nav.familie.ef.søknad

import org.springframework.boot.builder.SpringApplicationBuilder

class ApplicationLocalLauncher : ApplicationLocalTestLauncher()

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
            "mock-saf",
            "mock-saksbehandling",
        ).run(*args)
}
