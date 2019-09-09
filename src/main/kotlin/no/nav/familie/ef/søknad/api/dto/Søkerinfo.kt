package no.nav.familie.ef.søknad.api.dto

data class Søkerinfo(val søker: Person,
                     val arbeidsforhold: List<Arbeidsforhold>)
