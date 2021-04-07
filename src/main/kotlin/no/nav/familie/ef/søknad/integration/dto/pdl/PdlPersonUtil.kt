package no.nav.familie.ef.søknad.integration.dto.pdl

fun Navn.visningsnavn(): String {
    return this.let {
        val mellomnavn = it.mellomnavn.takeUnless { mellomnavn -> mellomnavn.isNullOrEmpty() }?.let { " ${it} " } ?: " "
        it.fornavn + mellomnavn + it.etternavn
    }
}