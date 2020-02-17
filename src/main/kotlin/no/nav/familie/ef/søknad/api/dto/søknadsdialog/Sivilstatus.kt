package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Sivilstatus(
    val begrunnelseForSøknad: BegrunnelseForSøknad? = null,
    val søkerGiftIUtlandet: SøkerGiftIUtlandet? = null,
    val søkerSeparertEllerSkiltIUtlandet: SøkerSeparertEllerSkiltIUtlandet? = null
)