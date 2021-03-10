package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Bosituasjon(
        val delerBoligMedAndreVoksne: TekstFelt,
        val datoFlyttetSammenMedSamboer: DatoFelt?,
        val samboerDetaljer: SamboerDetaljer?,
        val datoSkalGifteSegEllerBliSamboer: DatoFelt? = null,
        val skalGifteSegEllerBliSamboer: BooleanFelt?,
        val datoFlyttetFraHverandre: DatoFelt?,
        val vordendeSamboerEktefelle: SamboerDetaljer?,
        )

data class SamboerDetaljer(
        val fødselsdato: DatoFelt?,
        val navn: TekstFelt,
        val ident: TekstFelt? = null
)



