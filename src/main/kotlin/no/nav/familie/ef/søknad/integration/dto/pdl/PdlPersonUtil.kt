package no.nav.familie.ef.sak.integration.dto.pdl

fun List<Navn>.gjeldende(): Navn = this.maxByOrNull {
    it.metadata.endringer.maxByOrNull(MetadataEndringer::registrert)!!.registrert
}!!

fun Navn.visningsnavn(): String {
    return if (mellomnavn == null) "$fornavn $etternavn"
    else "$fornavn $mellomnavn $etternavn"
}
