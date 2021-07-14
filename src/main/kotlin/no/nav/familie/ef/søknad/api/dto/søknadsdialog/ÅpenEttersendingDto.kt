package no.nav.familie.ef.søknad.api.dto.søknadsdialog;

import no.nav.familie.kontrakter.ef.søknad.Dokument

data class ÅpenEttersendingDto (val person: Person,
                                val opplastedeVedlegg: List<Dokument>){

}