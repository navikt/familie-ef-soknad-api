package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.tps.Barn
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

    fun logDiff(søkerinfoV1: Søkerinfo, søkerinfoV2: Søkerinfo): String {
        val builder = StringBuilder()
        leggTilSøkerDiff(søkerinfoV1, søkerinfoV2, builder)
        leggTilBarneDiff(søkerinfoV1, søkerinfoV2, builder)
        if (builder.isNotBlank()) {
            secureLogger.warn(builder.toString())
        }
        return builder.toString()
    }

    private fun leggTilBarneDiff(søkerinfoV1: Søkerinfo,
                                 søkerinfoV2: Søkerinfo,
                                 builder: StringBuilder) {
        if (søkerinfoV1.barn.size != søkerinfoV2.barn.size) {
            builder.append("Ikke samme antall barn fra tps og pdl: \n V1: ${søkerinfoV1.barn}, \n V2: ${søkerinfoV2.barn}")
        } else {
            val barn1Liste = søkerinfoV1.barn.sortedBy { it.fnr }
            val barn2Liste = søkerinfoV2.barn.sortedBy { it.fnr }
            val zip = barn1Liste.zip(barn2Liste)
            zip.forEach {
                for (prop in Barn::class.memberProperties) {
                    val detErDiff = when (prop.name) {
                        "navn" -> {
                            listOfSeparatedNames(it.first.navn) != listOfSeparatedNames(it.second.navn)
                        }
                        else -> prop.get(it.first) != prop.get(it.second)
                    }
                    if (detErDiff) {
                        builder.append("\n Barn: ${prop.name} = V1: ${prop.get(it.first)}, V2: ${prop.get(it.second)}")
                    }
                }
            }
        }
    }

    private fun listOfSeparatedNames(name: String) =
            name.split(" ").sorted()

    private fun leggTilSøkerDiff(søkerinfoV1: Søkerinfo,
                                 søkerinfoV2: Søkerinfo,
                                 builder: StringBuilder) {
        for (prop in Person::class.memberProperties) {
            val property1 = prop.get(søkerinfoV1.søker)
            val property2 = prop.get(søkerinfoV2.søker)
            when (prop.name) {
                "sivilstand" -> {
                    logSivilstandsDiff(builder, søkerinfoV1.søker.sivilstand, søkerinfoV2.søker.sivilstand)
                }
                else -> {
                    if (property1 != property2 && prop.name != "sivilstand") {
                        builder.append("\n Person: ${prop.name} = V1: $property1, V2: $property2")
                    }
                }
            }
        }
    }

    private fun logSivilstandsDiff(builder: StringBuilder, sivilstand1: String, sivilstand2: String) {

        val skalLogge = !when (sivilstand1) {
            "SEPA", "SEPARERT" -> {
                listOf("SEPA", "SEPARERT").contains(sivilstand2)
            }
            "UGIF", "UGIFT" -> {
                listOf("UGIF", "UGIFT").contains(sivilstand2)
            }
            "SKIL", "SKILT" -> {
                listOf("SKIL", "SKILT").contains(sivilstand2)
            }
            "ENKE", "ENKE_ELLER_ENKEMANN" -> {
                listOf("ENKE", "ENKE_ELLER_ENKEMANN").contains(sivilstand2)
            }
            else -> sivilstand1 == sivilstand2
        }

        if (skalLogge) {
            builder.append(builder.append("\n Person: sivilstand = V1: $sivilstand1, V2: $sivilstand2"))
        }


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



