package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import java.time.LocalDate

data class Bosituasjon(
        val delerBoligMedAndreVoksne: TekstFelt,
        val datoFlyttetSammenMedSamboer: DatoFelt?,
        val samboerDetaljer: SamboerDetaljer?,
        val datoSkalGifteSegEllerBliSamboer: DatoFelt? = null,
        val skalGifteSegEllerBliSamboer: BooleanFelt
)

data class SamboerDetaljer(
        val fødselsnummer: String?,
        val fødselsdato: LocalDate?,
        val navn: String
)



