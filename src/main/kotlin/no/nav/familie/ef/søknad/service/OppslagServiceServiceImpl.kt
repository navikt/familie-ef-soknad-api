package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
internal class OppslagServiceServiceImpl(private val client: TpsInnsynServiceClient,
                                         private val pdlClient: PdlClient,
                                         private val pdlStsClient: PdlStsClient,
                                         private val regelverkConfig: RegelverkConfig,
                                         private val søkerinfoMapper: SøkerinfoMapper) : OppslagService {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    override fun hentSøkerinfo(): Søkerinfo {
        val personinfoDto = client.hentPersoninfo()
        val barn = client.hentBarn()
        secureLogger.info("Personinfo: ${personinfoDto.ident}, Barn alder/samme adresse : ${
            barn.map { Pair(it.alder, it.harSammeAdresse) }
        },  ")
        val aktuelleBarn = barn.filter { erIAktuellAlder(it.fødselsdato) }
        return settNavnFraPdlPåSøkerinfo(søkerinfoMapper.mapTilSøkerinfo(personinfoDto, aktuelleBarn))
    }

    override fun hentSøkerinfoV2(): Søkerinfo {
        val pdlSøker = pdlClient.hentSøker(EksternBrukerUtils.hentFnrFraToken())

        secureLogger.warn("pdlSoker: $pdlSøker")

        val barnIdentifikatorer = pdlSøker.familierelasjoner
                .filter { it.minRolleForPerson != Familierelasjonsrolle.BARN }
                .map { it.relatertPersonsIdent }

        secureLogger.warn("pdlbarnIdentifikatorerSoker: $barnIdentifikatorer")

        val hentBarnString = pdlStsClient.hentBarnString(barnIdentifikatorer)

        secureLogger.warn("hentBarnString: $hentBarnString")

        val pdlBarn = pdlStsClient.hentBarn(barnIdentifikatorer)

        secureLogger.warn("pdlBarn: $pdlBarn")

        val aktuelleBarn = pdlBarn.filter { erIAktuellAlder(it.value.fødsel.last().fødselsdato) }

        secureLogger.warn("aktuelleBarn: $aktuelleBarn", aktuelleBarn)

        return søkerinfoMapper.mapTilSøkerinfo(pdlSøker, pdlBarn)

    }

    private fun settNavnFraPdlPåSøkerinfo(søkerinfo: Søkerinfo): Søkerinfo {
        val pdlSøker = pdlClient.hentSøker(EksternBrukerUtils.hentFnrFraToken())
        val søker = søkerinfo.søker
        val mellomnavn = pdlSøker.navn.last().mellomnavn?.let { " $it " } ?: " "
        val oppdaterSøker =
                søker.copy(forkortetNavn = "${pdlSøker.navn.last().fornavn}$mellomnavn${pdlSøker.navn.last().etternavn}")
        return søkerinfo.copy(søker = oppdaterSøker)
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



