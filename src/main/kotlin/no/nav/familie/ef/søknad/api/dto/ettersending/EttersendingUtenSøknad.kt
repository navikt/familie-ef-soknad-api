package no.nav.familie.ef.søknad.api.dto.ettersending
import no.nav.familie.kontrakter.ef.felles.StønadType

data class EttersendingUtenSøknad(val stønadstype: StønadType, val innsending: List<Innsending>)
