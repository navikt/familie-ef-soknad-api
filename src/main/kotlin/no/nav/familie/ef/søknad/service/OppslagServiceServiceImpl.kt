package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.tps.Adresse
import no.nav.familie.ef.søknad.api.dto.tps.Barn
import no.nav.familie.ef.søknad.api.dto.tps.Person
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import no.nav.familie.ef.søknad.integration.dto.pdl.*
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
internal class OppslagServiceServiceImpl(private val client: TpsInnsynServiceClient,
                                         private val pdlClient: PdlClient,
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
        val søkerinfo = hentSøkerinfo()
        return settNavnFraPdlPåSøkerinfo(søkerinfo)
    }

    private fun settNavnFraPdlPåSøkerinfo(søkerinfo: Søkerinfo): Søkerinfo {
        val pdlSøker = pdlClient.hentSøker(EksternBrukerUtils.hentFnrFraToken())
        val søker = søkerinfo.søker
        val mellomnavn = pdlSøker.navn.last().mellomnavn?.let { " $it " } ?: " "
        val oppdaterSøker =
                søker.copy(forkortetNavn = "${pdlSøker.navn.last().fornavn}$mellomnavn${pdlSøker.navn.last().etternavn}")
        return søkerinfo.copy(søker = oppdaterSøker)
    }

    override fun hentSøkerinfoV3(): Søkerinfo {
        val pdlSøker = pdlClient.hentSøker(EksternBrukerUtils.hentFnrFraToken())

        secureLogger.warn("pdlSoker")

        val barnIdentifikatorer = pdlSøker.familierelasjoner
                .filter { it.minRolleForPerson == Familierelasjonsrolle.BARN }
                .map { it.relatertPersonsIdent }
        val pdlBarn = pdlClient.hentBarn(barnIdentifikatorer)
        val søker: Person = pdlSøker.tilPersonDto()
        val barn: List<Barn> = tilBarneListeDto(pdlBarn, pdlSøker.bostedsadresse.last().vegadresse)
        return Søkerinfo(søker, barn)
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


private fun tilBarneListeDto(pdlBarn: Map<String, PdlBarn>, søkersAdresse: Vegadresse?): List<Barn> {


    // TODO Finn bor hos (matrikkelId?)
    // Todo filter på alder -> erIAktuellAlder
    // TODO dobbeltsjekk "doedsfall"! (ikke med i query ennå)

    pdlBarn.mapValues {
        val mellomnavn = it.value.navn.last().mellomnavn?.let { " $it " } ?: " "
        val navn = it.value.navn.last().fornavn + mellomnavn + it.value.navn.last().etternavn
        val fødselsdato = it.value.fødsel.last().fødselsdato ?: error("Ingen fødselsdato registrert")
        val alder = Period.between(fødselsdato, LocalDate.now()).years

        // TODO - denne fungerer dårlig!! Se metrikkelId - usikker på om denne er implementer?
        val harSammeAdresse =
                søkersAdresse == it.value.bostedsadresse.last().vegadresse
//      Fra PDL dokumentasjon - hva betyr dette?
//       3.6.1. TPS
//       TPS tilbyr informasjon om hvem som har samme bosted sammen med familierelasjoner.
//       I PDLs bostedsadresser har man en matrikkelId som kan benyttes for å sammenligne om
//       personer med relasjoner har samme bosted.
//       Dersom matrikkelId er lik er de registrert som bosatt på samme adresse.


        Barn(it.key, navn, alder, fødselsdato, harSammeAdresse)
    }


    return listOf<Barn>()
}

private fun PdlSøker.tilPersonDto(): Person {
    // TODO poststed
    val adresse = Adresse(adresse = tilFormatertAdresse(bostedsadresse.last().vegadresse),
                          poststed = "",
                          postnummer = bostedsadresse.last().vegadresse?.postnummer ?: " ")
    return Person(fnr = EksternBrukerUtils.hentFnrFraToken(),
                  forkortetNavn = navn.last().visningsnavn(),
                  adresse = adresse,
                  egenansatt = false,
                  sivilstand = sivilstand.last().type.toString(),
                  statsborgerskap = statsborgerskap.last().land)
}

private fun tilFormatertAdresse(vegadresse: Vegadresse?): String {

    return join(space(vegadresse?.adressenavn ?: "",
                      vegadresse?.husnummer ?: "",
                      vegadresse?.husbokstav ?: "",
                      vegadresse?.bruksenhetsnummer ?: "")) ?: ""
}

private fun join(vararg args: String?, separator: String = ", "): String? {
    val filterNotNull = args.filterNotNull().filterNot(String::isEmpty)
    return if (filterNotNull.isEmpty()) {
        null
    } else filterNotNull.joinToString(separator)
}

private fun space(vararg args: String?): String? = join(*args, separator = " ")

