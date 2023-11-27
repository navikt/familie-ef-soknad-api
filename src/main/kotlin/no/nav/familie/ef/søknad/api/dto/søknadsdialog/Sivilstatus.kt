package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Sivilstatus(
    val årsakEnslig: TekstFelt? = null,
    val datoForSamlivsbrudd: DatoFelt? = null,
    val datoFlyttetFraHverandre: DatoFelt? = null,
    val datoEndretSamvær: DatoFelt? = null,
    val tidligereSamboerDetaljer: SamboerDetaljer? = null,
)
