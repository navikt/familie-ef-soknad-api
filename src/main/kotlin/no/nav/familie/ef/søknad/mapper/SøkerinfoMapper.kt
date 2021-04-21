package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.pdl.Adresse
import no.nav.familie.ef.søknad.api.dto.pdl.Barn
import no.nav.familie.ef.søknad.api.dto.pdl.Medforelder
import no.nav.familie.ef.søknad.api.dto.pdl.Person
import no.nav.familie.ef.søknad.integration.dto.pdl.Adressebeskyttelse
import no.nav.familie.ef.søknad.integration.dto.pdl.AdressebeskyttelseGradering
import no.nav.familie.ef.søknad.integration.dto.pdl.Bostedsadresse
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjon
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle
import no.nav.familie.ef.søknad.integration.dto.pdl.MatrikkelId
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlAnnenForelder
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

    fun hentPoststed(postnummer: String?): String {
        return hentKodeverdi("poststed", postnummer, kodeverkService::hentPoststed)
    }

    fun hentLand(landkode: String?): String {
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

    fun mapTilSøkerinfo(pdlSøker: PdlSøker,
                        pdlBarn: Map<String, PdlBarn>,
                        andreForeldre: Map<String, PdlAnnenForelder>): Søkerinfo {
        val søker: Person = pdlSøker.tilPersonDto()
        val barn: List<Barn> = tilBarneListeDto(pdlBarn, pdlSøker.bostedsadresse.firstOrNull(), andreForeldre, søker.fnr)
        return Søkerinfo(søker, barn)
    }


    private fun tilBarneListeDto(pdlBarn: Map<String, PdlBarn>,
                                 søkersAdresse: Bostedsadresse?,
                                 andreForeldre: Map<String, PdlAnnenForelder>,
                                 søkerPersonIdent: String): List<Barn> {
        return pdlBarn.entries.map {
            val navn = it.value.navn.firstOrNull()?.visningsnavn() ?: ""
            val fødselsdato = it.value.fødsel.firstOrNull()?.fødselsdato ?: error("Ingen fødselsdato registrert")
            val alder = Period.between(fødselsdato, LocalDate.now()).years

            val harSammeAdresse = harSammeAdresse(søkersAdresse, it.value)

            val medforelderRelasjon = it.value.familierelasjoner.find { erMedForelderRelasjon(it, søkerPersonIdent) }
            val medforelder =
                    medforelderRelasjon?.let { andreForeldre[it.relatertPersonsIdent]?.tilDto(it.relatertPersonsIdent) }
            Barn(it.key,
                 navn,
                 alder,
                 fødselsdato,
                 harSammeAdresse,
                 medforelder,
                 it.value.adressebeskyttelse.harBeskyttetAdresse())
        }
    }

    private fun erMedForelderRelasjon(familierelasjon: Familierelasjon,
                                      søkersPersonIdent: String) =
            familierelasjon.relatertPersonsIdent != søkersPersonIdent &&
            familierelasjon.relatertPersonsRolle != Familierelasjonsrolle.BARN

    fun harSammeAdresse(søkersAdresse: Bostedsadresse?, pdlBarn: PdlBarn): Boolean {
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
                      statsborgerskap = statsborgerskapListe,
                      harAdressesperre = adressebeskyttelse.harBeskyttetAdresse()
        )
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
            join(
                    space(
                            vegadresse.adressenavn ?: "",
                            vegadresse.husnummer ?: "",
                            vegadresse.husbokstav ?: "",
                            vegadresse.bruksenhetsnummer ?: ""
                    )
            ) ?: ""

    private fun join(vararg args: String?, separator: String = ", "): String? {
        val filterNotNull = args.filterNotNull().filterNot(String::isEmpty)
        return if (filterNotNull.isEmpty()) {
            null
        } else filterNotNull.joinToString(separator)
    }

    private fun space(vararg args: String?): String? = join(*args, separator = " ")


}

fun PdlAnnenForelder.tilDto(annenForelderPersonsIdent: String): Medforelder {
    val annenForelderNavn = this.navn.first()


    return Medforelder(annenForelderNavn.visningsnavn(),
                       this.adressebeskyttelse.harBeskyttetAdresse(), this.dødsfall.any(), annenForelderPersonsIdent)
}

fun List<Adressebeskyttelse>.harBeskyttetAdresse(): Boolean = kreverAdressebeskyttelse.contains(this.firstOrNull()?.gradering)


private val kreverAdressebeskyttelse = listOf(
        AdressebeskyttelseGradering.FORTROLIG,
        AdressebeskyttelseGradering.STRENGT_FORTROLIG,
        AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND
)