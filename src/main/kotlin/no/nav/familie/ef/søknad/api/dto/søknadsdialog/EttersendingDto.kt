package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class EttersendingDto(val fnr: String, val søknadMedVedlegg: SøknadMedVedlegg?,
                            val åpenInnsendingMedStønadType: ÅpenInnsendingMedStønadstype?
)
