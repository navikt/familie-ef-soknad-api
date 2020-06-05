package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Bosituasjon(
        val delerBoligMedAndreVoksne: TekstFelt,
        val datoFlyttetSammenMedSamboer: DatoFelt?,
        val samboerDetaljer: SamboerDetaljer?,
        val datoSkalGifteSegEllerBliSamboer: DatoFelt? = null,
        val skalGifteSegEllerBliSamboer: BooleanFelt
)

data class SamboerDetaljer(
        val fødselsnummer: String?,
        val fødselsdato: DatoFelt?,
        val navn: String
)



