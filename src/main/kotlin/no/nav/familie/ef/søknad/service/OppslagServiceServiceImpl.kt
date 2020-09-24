package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import org.apache.commons.logging.LogFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
internal class OppslagServiceServiceImpl(private val client: TpsInnsynServiceClient,
                                         private val regelverkConfig: RegelverkConfig,
                                         private val søkerinfoMapper: SøkerinfoMapper) : OppslagService {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")
    private val logger = LogFactory.getLog(this.javaClass)


    override fun hentSøkerinfo(): Søkerinfo {
        val personinfoDto = client.hentPersoninfo()
        val barn = client.hentBarn()
        secureLogger.info("Personinfo: ${personinfoDto.ident}, Barn alder/samme adresse : ${
            barn.map { Pair(it.alder, it.harSammeAdresse) }
        },  ")
        val aktuelleBarn = barn.filter { erIAktuellAlder(it.fødselsdato) }
        return søkerinfoMapper.mapTilSøkerinfo(personinfoDto, aktuelleBarn)
    }

    fun erIAktuellAlder(fødselsdato: LocalDate?): Boolean {
        if (fødselsdato == null) {
            return false
        }
        val alder = Period.between(fødselsdato, LocalDate.now())
        val alderIÅr = alder.years
        return alderIÅr <= regelverkConfig.alder.maks
    }

}
