package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Sivilstatus
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.*
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.slf4j.LoggerFactory

object SivilstandsdetaljerMapper {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun mapSivilstandsdetaljer(sivilstatus: Sivilstatus, vedlegg: Map<String, DokumentasjonWrapper>): Sivilstandsdetaljer {
        try {
            return Sivilstandsdetaljer(samlivsbruddsdokumentasjon = dokumentfelt(SAMLIVSBRUDD, vedlegg),
                                       samlivsbruddsdato = sivilstatus.datoForSamlivsbrudd?.tilSøknadsfelt(),
                                       endringSamværsordningDato = sivilstatus.datoEndretSamvær?.tilSøknadsfelt(),
                                       fraflytningsdato = sivilstatus.datoFlyttetFraHverandre?.tilSøknadsfelt(),
                                       erUformeltGift = sivilstatus.erUformeltGift?.tilSøknadsfelt(),
                                       erUformeltGiftDokumentasjon = dokumentfelt(INNGÅTT_EKTESKAP, vedlegg),
                                       separasjonsbekreftelse = dokumentfelt(SEPARASJON_ELLER_SKILSMISSE, vedlegg),
                                       erUformeltSeparertEllerSkilt = sivilstatus.erUformeltSeparertEllerSkilt?.tilSøknadsfelt(),
                                       erUformeltSeparertEllerSkiltDokumentasjon =
                                       dokumentfelt(UFORMELL_SEPARASJON_ELLER_SKILSMISSE, vedlegg),
                                       datoSøktSeparasjon = sivilstatus.datoSøktSeparasjon?.tilSøknadsfelt(),
                                       søktOmSkilsmisseSeparasjon = sivilstatus.harSøktSeparasjon?.tilSøknadsfelt(),
                                       årsakEnslig = sivilstatus.årsakEnslig?.tilSøknadsfelt(),
                                       tidligereSamboerdetaljer = sivilstatus.tidligereSamboerDetaljer?.let {
                                           Søknadsfelt("Om den tidligere samboeren din",
                                                       PersonMinimumMapper.personMinimum(it))
                                       })
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av sivilstatus: $sivilstatus")
            throw e
        }
    }
}
