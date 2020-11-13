package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
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
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun hentSøkerinfo(): Søkerinfo {

        try {
            hentSøkerinfoV2()
        } catch (e: Exception) {
            secureLogger.info("Exception - hent søker fra pdl", e)
            logger.warn("Exception - hent søker fra pdl (se securelogs for detaljer)")
        }

        val personinfoDto = client.hentPersoninfo()
        val barn = client.hentBarn()
        val aktuelleBarn = barn.filter { erIAktuellAlder(it.fødselsdato) }
        return settNavnFraPdlPåSøkerinfo(søkerinfoMapper.mapTilSøkerinfo(personinfoDto, aktuelleBarn))
    }

    override fun hentSøkerinfoV2(): Søkerinfo {
        val pdlSøker = pdlClient.hentSøker(EksternBrukerUtils.hentFnrFraToken())
        val barnIdentifikatorer = pdlSøker.familierelasjoner
                .filter { it.relatertPersonsRolle == Familierelasjonsrolle.BARN }
                .map { it.relatertPersonsIdent }
        val pdlBarn = pdlStsClient.hentBarn(barnIdentifikatorer)
        // TODO trenger vi sjekk/filtrering av foreldreansvar
        val aktuelleBarn = pdlBarn
                .filter { erIAktuellAlder(it.value.fødsel.first().fødselsdato) }
                .filter { erILive(it.value) }
        return søkerinfoMapper.mapTilSøkerinfo(pdlSøker, aktuelleBarn)
    }

    fun erILive(pdlBarn: PdlBarn) =
            pdlBarn.dødsfall.firstOrNull()?.dødsdato == null

    private fun settNavnFraPdlPåSøkerinfo(søkerinfo: Søkerinfo): Søkerinfo {
        val pdlSøker = pdlClient.hentSøker(EksternBrukerUtils.hentFnrFraToken())
        val søker = søkerinfo.søker
        val mellomnavn = pdlSøker.navn.first().mellomnavn?.let { " $it " } ?: " "
        val oppdaterSøker =
                søker.copy(forkortetNavn = "${pdlSøker.navn.first().fornavn}$mellomnavn${pdlSøker.navn.first().etternavn}")
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



