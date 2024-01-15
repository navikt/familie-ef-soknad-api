package no.nav.familie.ef.søknad.søknad.domain

import java.time.LocalDateTime

data class Kvittering(val text: String, val mottattDato: LocalDateTime?)
