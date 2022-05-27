package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Sivilstatus(
    val harSøktSeparasjon: BooleanFelt?, // TODO Burde ikke være optional - default false i UI?
    val datoSøktSeparasjon: DatoFelt? = null,
    val erUformeltGift: BooleanFelt?, // TODO Burde ikke være optional - default false i UI ?
    val erUformeltSeparertEllerSkilt: BooleanFelt?, // TODO Burde ikke være optional - default false i UI ?
    val årsakEnslig: TekstFelt? = null,
    val datoForSamlivsbrudd: DatoFelt? = null,
    val datoFlyttetFraHverandre: DatoFelt? = null,
    val datoEndretSamvær: DatoFelt? = null,
    val tidligereSamboerDetaljer: SamboerDetaljer? = null
)
