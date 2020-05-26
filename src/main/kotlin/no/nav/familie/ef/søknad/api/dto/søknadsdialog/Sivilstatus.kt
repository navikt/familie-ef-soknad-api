package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Sivilstatus(val harSøktSeparasjon: BooleanFelt,
                       val datoSøktSeparasjon: DatoFelt? = null,
                       val erUformeltGift: BooleanFelt,
                       val erUformeltSeparertEllerSkilt: BooleanFelt,
                       val årsakEnslig: TekstFelt? = null,
                       val datoForSamlivsbrudd: DatoFelt? = null,
                       val datoFlyttetFraHverandre: DatoFelt? = null,
                       val datoEndretSamvær: DatoFelt? = null)


