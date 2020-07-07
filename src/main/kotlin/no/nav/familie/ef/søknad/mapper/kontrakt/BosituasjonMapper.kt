package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.BOR_PÅ_ULIKE_ADRESSER
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.slf4j.LoggerFactory
import no.nav.familie.kontrakter.ef.søknad.Bosituasjon as KontraktBosituasjon

object BosituasjonMapper {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun mapBosituasjon(bosituasjon: Bosituasjon, vedlegg: Map<String, DokumentasjonWrapper>): KontraktBosituasjon {
        try {

            return KontraktBosituasjon(delerDuBolig = mapSøkerDelerBoligMedAndre(bosituasjon),
                                       samboerdetaljer = mapSamboer(bosituasjon),
                                       sammenflyttingsdato = bosituasjon.datoFlyttetSammenMedSamboer?.tilSøknadsfelt(),
                                       tidligereSamboerFortsattRegistrertPåAdresse = dokumentfelt(BOR_PÅ_ULIKE_ADRESSER, vedlegg),
                                       datoFlyttetFraHverandre = bosituasjon.datoFlyttetFraHverandre?.tilSøknadsfelt())
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av bosituasjon: $bosituasjon")
            throw e
        }
    }

    private fun mapSøkerDelerBoligMedAndre(bosituasjon: Bosituasjon) = bosituasjon.delerBoligMedAndreVoksne.tilSøknadsfelt()

    private fun mapSamboer(bosituasjon: Bosituasjon): Søknadsfelt<PersonMinimum>? {
        return bosituasjon.samboerDetaljer?.let {
            PersonMinimumMapper.map(it)
        }
    }
}
