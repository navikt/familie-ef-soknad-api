package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.tps.Person
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
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
import kotlin.reflect.full.memberProperties

@Service
internal class OppslagServiceServiceImpl(private val client: TpsInnsynServiceClient,
                                         private val featureToggleService: FeatureToggleService,
                                         private val pdlClient: PdlClient,
                                         private val pdlStsClient: PdlStsClient,
                                         private val regelverkConfig: RegelverkConfig,
                                         private val søkerinfoMapper: SøkerinfoMapper) : OppslagService {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun hentSøkerinfo(): Søkerinfo {
        val personinfoDto = client.hentPersoninfo()
        val barn = client.hentBarn()
        val aktuelleBarn = barn.filter { erIAktuellAlder(it.fødselsdato) }
        val søkerinfoDto = settNavnFraPdlPåSøkerinfo(søkerinfoMapper.mapTilSøkerinfo(personinfoDto, aktuelleBarn))

        try {
            val hentSøkerinfoV2 = hentSøkerinfoV2()
            logDiff(søkerinfoDto, hentSøkerinfoV2)
            if (featureToggleService.isEnabled("familie.ef.soknad.bruk-pdl")) {
                return hentSøkerinfoV2
            }
        } catch (e: Exception) {
            secureLogger.info("Exception - hent søker fra pdl", e)
            logger.warn("Exception - hent søker fra pdl (se securelogs for detaljer)")
        }


        return søkerinfoDto
    }

    fun logDiff(søkerinfoV1: Søkerinfo, søkerinfoV2: Søkerinfo) {

        val builder = StringBuilder()

        for (prop in Person::class.memberProperties) {
            val property1 = prop.get(søkerinfoV1.søker)
            val property2 = prop.get(søkerinfoV2.søker)
            if (property1 != property2) {
                builder.append("Person: ${prop.name} = V1: $property1, V2: $property2")
            }
        }

        if (søkerinfoV1.barn != søkerinfoV2.barn) {
            builder.append("Ulike barn: V1: ${søkerinfoV1.barn}, V2: ${søkerinfoV2.barn}")
        }

        return secureLogger.warn(builder.toString())

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



