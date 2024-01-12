package no.nav.familie.ef.søknad.person.dto

fun Navn.visningsnavn(): String {
    return if (mellomnavn.isNullOrEmpty()) {
        "$fornavn $etternavn"
    } else {
        "$fornavn $mellomnavn $etternavn"
    }
}
