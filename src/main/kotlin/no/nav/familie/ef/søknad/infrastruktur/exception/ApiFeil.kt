package no.nav.familie.ef.søknad.infrastruktur.exception

import org.springframework.http.HttpStatus

data class ApiFeil(val feil: String, val httpStatus: HttpStatus) : RuntimeException()
