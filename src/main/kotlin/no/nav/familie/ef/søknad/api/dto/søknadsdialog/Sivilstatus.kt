package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Sivilstatus(val søkerHarSøktSeparasjon: BooleanFelt? = null,
                       val datoSøktSeparasjon: DatoFelt? = null,
                       val søkerGiftIUtlandet: BooleanFelt? = null,
                       val søkerSeparertEllerSkiltIUtlandet: BooleanFelt? = null,
                       val begrunnelseForSøknad: TekstFelt? = null,
                       val datoForSamlivsbrudd: DatoFelt? = null,
                       val datoFlyttetFraHverandre: DatoFelt? = null,
                       val datoEndretSamvær: DatoFelt? = null,
                       val begrunnelseAnnet: TekstFelt? = null)
