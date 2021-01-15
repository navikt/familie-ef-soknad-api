package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.tps.Adresse
import no.nav.familie.ef.søknad.api.dto.tps.Barn
import no.nav.familie.ef.søknad.api.dto.tps.Person
import no.nav.familie.ef.søknad.integration.dto.AdresseinfoDto
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.integration.dto.pdl.*
import no.nav.familie.ef.søknad.service.KodeverkService
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Period

@Component
internal class SøkerinfoMapper(private val kodeverkService: KodeverkService) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

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
            kode?.let(hentKodeverdiFunction) ?: kode ?: ""
        } catch (e: Exception) {
            //Ikke la feil fra integrasjon stoppe henting av data
            logger.error("Feilet henting av $type til $kode message=${e.message} cause=${e.cause?.message}")
            ""
        }
    }

    fun mapTilSøkerinfo(pdlSøker: PdlSøker, pdlBarn: Map<String, PdlBarn>): Søkerinfo {
        val søker: Person = pdlSøker.tilPersonDto()
        val barn: List<Barn> = tilBarneListeDto(pdlBarn, pdlSøker.bostedsadresse.firstOrNull())
        return Søkerinfo(søker, barn)
    }


    private fun tilBarneListeDto(pdlBarn: Map<String, PdlBarn>, søkersAdresse: Bostedsadresse?): List<Barn> {
        return pdlBarn.entries.map {
            val mellomnavn = it.value.navn.first().mellomnavn?.let { " $it " } ?: " "
            val navn = it.value.navn.first().fornavn + mellomnavn + it.value.navn.first().etternavn
            val fødselsdato = it.value.fødsel.first().fødselsdato ?: error("Ingen fødselsdato registrert")
            val alder = Period.between(fødselsdato, LocalDate.now()).years

            val harSammeAdresse = harSammeAdresse(søkersAdresse, it.value)
            if (!harSammeAdresse) {
                secureLogger.info("Søkers adresse: [${søkersAdresse?.vegadresse}] - Barnets adresse: [${it.value.bostedsadresse.firstOrNull()?.vegadresse}]")
            }
            Barn(it.key, navn, alder, fødselsdato, harSammeAdresse)
        }
    }

    fun harSammeAdresse(søkersAdresse: Bostedsadresse?,
                        pdlBarn: PdlBarn): Boolean {
        val barnetsAdresse = pdlBarn.bostedsadresse.firstOrNull()
        if (søkersAdresse == null || barnetsAdresse == null || harDeltBosted(pdlBarn)) {
            return false
        }

        return if (søkersAdresse.vegadresse?.matrikkelId != null
                   && søkersAdresse.vegadresse.matrikkelId == barnetsAdresse.vegadresse?.matrikkelId) {
            true
        } else if (søkersAdresse.matrikkeladresse?.matrikkelId != null
                   && søkersAdresse.matrikkeladresse.matrikkelId == barnetsAdresse.matrikkeladresse?.matrikkelId) {
            true
        } else {
            if (harIkkeMatrikkelId(søkersAdresse.vegadresse, søkersAdresse.matrikkeladresse)
                && harIkkeMatrikkelId(barnetsAdresse.vegadresse, barnetsAdresse.matrikkeladresse)) {
                logger.info("Finner ikke matrikkelId på noen av adressene")
            }
            return søkersAdresse.vegadresse != null && søkersAdresse.vegadresse == barnetsAdresse.vegadresse
        }
    }

    private fun harDeltBosted(pdlBarn: PdlBarn) =
            pdlBarn.deltBosted.any {
                it.startdatoForKontrakt.isBefore(LocalDate.now())
                && (it.sluttdatoForKontrakt == null || it.sluttdatoForKontrakt.isAfter(LocalDate.now()))
            }

    private fun harIkkeMatrikkelId(vegadresse: Vegadresse?, matrikkeladresse: MatrikkelId?) =
            harIkkeMatrikkelId(vegadresse) || harIkkeMatrikkelId(matrikkeladresse)

    private fun harIkkeMatrikkelId(adresse: Vegadresse?) = adresse != null && adresse.matrikkelId == null
    private fun harIkkeMatrikkelId(adresse: MatrikkelId?) = adresse != null && adresse.matrikkelId == null

    private fun PdlSøker.tilPersonDto(): Person {
        val formatertAdresse = formaterAdresse(this)
        val adresse = Adresse(adresse = formatertAdresse,
                              poststed = hentPoststed(bostedsadresse.firstOrNull()?.vegadresse?.postnummer),
                              postnummer = bostedsadresse.firstOrNull()?.vegadresse?.postnummer ?: " ")

        val statsborgerskapListe = statsborgerskap.map { hentLand(it.land) }.joinToString(", ")

        return Person(fnr = EksternBrukerUtils.hentFnrFraToken(),
                      forkortetNavn = navn.first().visningsnavn(),
                      adresse = adresse,
                      egenansatt = false,
                      sivilstand = sivilstand.first().type.toString(),
                      statsborgerskap = statsborgerskapListe)
    }

    private fun formaterAdresse(pdlSøker: PdlSøker): String {
        val bosted = pdlSøker.bostedsadresse.firstOrNull()
        return when {
            bosted == null -> {
                logger.info("Finner ikke bostedadresse")
                ""
            }
            bosted.vegadresse != null -> {
                tilFormatertAdresse(bosted.vegadresse)
            }
            bosted.matrikkeladresse != null -> {
                join(bosted.matrikkeladresse.tilleggsnavn, hentPoststed(bosted.matrikkeladresse.postnummer)) ?: ""
            }
            else -> {
                logger.info("Søker har hverken vegadresse eller matrikkeladresse")
                ""
            }
        }
    }

    private fun tilFormatertAdresse(vegadresse: Vegadresse): String =
            join(space(vegadresse.adressenavn ?: "",
                       vegadresse.husnummer ?: "",
                       vegadresse.husbokstav ?: "",
                       vegadresse.bruksenhetsnummer ?: "")) ?: ""

    private fun join(vararg args: String?, separator: String = ", "): String? {
        val filterNotNull = args.filterNotNull().filterNot(String::isEmpty)
        return if (filterNotNull.isEmpty()) {
            null
        } else filterNotNull.joinToString(separator)
    }

    private fun space(vararg args: String?): String? = join(*args, separator = " ")

}
