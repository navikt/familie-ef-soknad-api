package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.tps.Adresse
import no.nav.familie.ef.søknad.api.dto.tps.Barn
import no.nav.familie.ef.søknad.api.dto.tps.Person
import no.nav.familie.ef.søknad.integration.dto.AdresseinfoDto
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlSøker
import no.nav.familie.ef.søknad.integration.dto.pdl.Vegadresse
import no.nav.familie.ef.søknad.integration.dto.pdl.visningsnavn
import no.nav.familie.ef.søknad.service.KodeverkService
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Period

@Component
internal class SøkerinfoMapper(private val kodeverkService: KodeverkService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun mapTilSøkerinfo(personinfoDto: PersoninfoDto, aktuelleBarn: List<RelasjonDto>): Søkerinfo {
        return Søkerinfo(mapTilPerson(personinfoDto),
                         aktuelleBarn.map(this::mapTilBarn))
    }

    fun mapTilBarn(relasjonDto: RelasjonDto): Barn {
        return Barn(relasjonDto.ident,
                    relasjonDto.forkortetNavn,
                    relasjonDto.alder,
                    relasjonDto.fødselsdato,
                    relasjonDto.harSammeAdresse)
    }


    fun mapTilPerson(personinfoDto: PersoninfoDto): Person {
        return Person(personinfoDto.ident,
                      personinfoDto.navn.forkortetNavn,
                      mapTilAdresse(personinfoDto.adresseinfo),
                      personinfoDto.egenansatt?.isErEgenansatt ?: false,
                      personinfoDto.sivilstand?.kode?.verdi ?: "",
                      hentLand(personinfoDto.statsborgerskap?.kode?.verdi))
    }

    private fun mapTilAdresse(adresseinfoDto: AdresseinfoDto?): Adresse {
        val postnummer: String? = adresseinfoDto?.bostedsadresse?.postnummer
        return Adresse(adresse = adresseinfoDto?.bostedsadresse?.adresse
                                 ?: "",
                       postnummer = postnummer ?: "",
                       poststed = hentPoststed(postnummer))
    }

    private fun hentPoststed(postnummer: String?): String {
        return hentKodeverdi("poststed", postnummer, kodeverkService::hentPoststed)
    }

    private fun hentLand(landkode: String?): String {
        return hentKodeverdi("land", landkode, kodeverkService::hentLand)
    }

    private fun hentKodeverdi(type: String, kode: String?, hentKodeverdiFunction: Function1<String, String?>): String {
        return try {
            kode?.let(hentKodeverdiFunction) ?: ""
        } catch (e: Exception) {
            //Ikke la feil fra integrasjon stoppe henting av data
            logger.error("Feilet henting av $type til $kode message=${e.message} cause=${e.cause?.message}")
            ""
        }
    }

    fun mapTilSøkerinfo(pdlSøker: PdlSøker, pdlBarn: Map<String, PdlBarn>): Søkerinfo {
        val søker: Person = pdlSøker.tilPersonDto()
        val barn: List<Barn> = tilBarneListeDto(pdlBarn, pdlSøker.bostedsadresse.last().vegadresse)
        return Søkerinfo(søker, barn)
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

}
