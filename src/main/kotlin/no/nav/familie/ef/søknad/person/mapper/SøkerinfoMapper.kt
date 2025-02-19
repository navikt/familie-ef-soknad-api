package no.nav.familie.ef.søknad.person.mapper

import no.nav.familie.ef.søknad.kodeverk.KodeverkService
import no.nav.familie.ef.søknad.person.domain.Adresse
import no.nav.familie.ef.søknad.person.domain.Barn
import no.nav.familie.ef.søknad.person.domain.Medforelder
import no.nav.familie.ef.søknad.person.domain.Person
import no.nav.familie.ef.søknad.person.domain.Søkerinfo
import no.nav.familie.ef.søknad.person.dto.Adressebeskyttelse
import no.nav.familie.ef.søknad.person.dto.AdressebeskyttelseGradering
import no.nav.familie.ef.søknad.person.dto.Bostedsadresse
import no.nav.familie.ef.søknad.person.dto.Familierelasjonsrolle
import no.nav.familie.ef.søknad.person.dto.ForelderBarnRelasjon
import no.nav.familie.ef.søknad.person.dto.Fødselsdato
import no.nav.familie.ef.søknad.person.dto.MatrikkelId
import no.nav.familie.ef.søknad.person.dto.PdlAnnenForelder
import no.nav.familie.ef.søknad.person.dto.PdlBarn
import no.nav.familie.ef.søknad.person.dto.PdlSøker
import no.nav.familie.ef.søknad.person.dto.Sivilstand
import no.nav.familie.ef.søknad.person.dto.Sivilstandstype
import no.nav.familie.ef.søknad.person.dto.Vegadresse
import no.nav.familie.ef.søknad.person.dto.visningsnavn
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Period

@Component
internal class SøkerinfoMapper(
    private val kodeverkService: KodeverkService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun hentPoststed(postnummer: String?): String = hentKodeverdi("poststed", postnummer, kodeverkService::hentPoststed)

    fun hentLand(landkode: String?): String = hentKodeverdi("land", landkode, kodeverkService::hentLand)

    private fun hentKodeverdi(
        type: String,
        kode: String?,
        hentKodeverdiFunction: Function1<String, String?>,
    ): String =
        try {
            kode?.let(hentKodeverdiFunction) ?: kode ?: ""
        } catch (e: Exception) {
            // Ikke la feil fra integrasjon stoppe henting av data
            logger.error("Feilet henting av $type til $kode message=${e.message} cause=${e.cause?.message}")
            ""
        }

    fun mapTilSøkerinfo(
        pdlSøker: PdlSøker,
        pdlBarn: Map<String, PdlBarn>,
        andreForeldre: Map<String, PdlAnnenForelder>,
    ): Søkerinfo {
        val søker: Person = pdlSøker.tilPersonDto()
        val barn: List<Barn> = tilBarneListeDto(pdlBarn, pdlSøker.bostedsadresse.firstOrNull(), andreForeldre, søker.fnr)
        return Søkerinfo(søker, barn)
    }

    private fun tilBarneListeDto(
        pdlBarn: Map<String, PdlBarn>,
        søkersAdresse: Bostedsadresse?,
        andreForeldre: Map<String, PdlAnnenForelder>,
        søkerPersonIdent: String,
    ): List<Barn> =
        pdlBarn.entries.map { (personIdent, pdlBarn) ->
            val barnNavnOgIdent =
                if (pdlBarn.adressebeskyttelse.harBeskyttetAdresse()) {
                    BarnNavnOgIdent()
                } else {
                    BarnNavnOgIdent(
                        personIdent,
                        pdlBarn.navn.firstOrNull()?.visningsnavn() ?: "",
                    )
                }

            val fødselsdato = pdlBarn.fødselsdato.firstOrNull()?.fødselsdato ?: error("Ingen fødselsdato registrert")
            val alder = Period.between(fødselsdato, LocalDate.now()).years

            val harSammeAdresse = harSammeAdresse(søkersAdresse, pdlBarn)

            val medforelderRelasjon = pdlBarn.forelderBarnRelasjon.find { erMedForelderRelasjon(it, søkerPersonIdent) }
            val medforelder =
                medforelderRelasjon
                    ?.relatertPersonsIdent
                    ?.let { relatertPersonsIdent -> andreForeldre[relatertPersonsIdent]?.tilDto(relatertPersonsIdent) }

            Barn(
                barnNavnOgIdent.ident,
                barnNavnOgIdent.navn,
                alder,
                fødselsdato,
                harSammeAdresse,
                medforelder,
                pdlBarn.adressebeskyttelse.harBeskyttetAdresse(),
            )
        }

    private fun erMedForelderRelasjon(
        forelderBarnRelasjon: ForelderBarnRelasjon,
        søkersPersonIdent: String,
    ) = forelderBarnRelasjon.relatertPersonsIdent != søkersPersonIdent &&
        forelderBarnRelasjon.relatertPersonsRolle != Familierelasjonsrolle.BARN

    fun harSammeAdresse(
        søkersAdresse: Bostedsadresse?,
        pdlBarn: PdlBarn,
    ): Boolean {
        val barnetsAdresse = pdlBarn.bostedsadresse.firstOrNull()

        if (harDeltBosted(pdlBarn)) {
            return true
        }

        if (søkersAdresse == null || barnetsAdresse == null) {
            return false
        }

        return if (søkersAdresse.vegadresse?.matrikkelId != null &&
            søkersAdresse.vegadresse.matrikkelId == barnetsAdresse.vegadresse?.matrikkelId
        ) {
            true
        } else if (søkersAdresse.matrikkeladresse?.matrikkelId != null &&
            søkersAdresse.matrikkeladresse.matrikkelId == barnetsAdresse.matrikkeladresse?.matrikkelId
        ) {
            true
        } else {
            if (harIkkeMatrikkelId(søkersAdresse.vegadresse, søkersAdresse.matrikkeladresse) &&
                harIkkeMatrikkelId(barnetsAdresse.vegadresse, barnetsAdresse.matrikkeladresse)
            ) {
                logger.info("Finner ikke matrikkelId på noen av adressene")
            }
            return søkersAdresse.vegadresse != null && søkersAdresse.vegadresse == barnetsAdresse.vegadresse
        }
    }

    private fun harDeltBosted(pdlBarn: PdlBarn) =
        pdlBarn.deltBosted.any {
            it.startdatoForKontrakt.isBefore(LocalDate.now()) &&
                (it.sluttdatoForKontrakt == null || it.sluttdatoForKontrakt.isAfter(LocalDate.now()))
        }

    private fun harIkkeMatrikkelId(
        vegadresse: Vegadresse?,
        matrikkeladresse: MatrikkelId?,
    ) = harIkkeMatrikkelId(vegadresse) || harIkkeMatrikkelId(matrikkeladresse)

    private fun harIkkeMatrikkelId(adresse: Vegadresse?) = adresse != null && adresse.matrikkelId == null

    private fun harIkkeMatrikkelId(adresse: MatrikkelId?) = adresse != null && adresse.matrikkelId == null

    private fun PdlSøker.tilPersonDto(): Person {
        val formatertAdresse = formaterAdresse(this)
        val adresse =
            Adresse(
                adresse = formatertAdresse,
                poststed = hentPoststed(bostedsadresse.firstOrNull()?.vegadresse?.postnummer),
                postnummer = bostedsadresse.firstOrNull()?.vegadresse?.postnummer ?: " ",
            )
        statsborgerskap.forEach {logger.info("Statsborgerskap TEST:" + it.metadata?.historisk.toString() + it.land)}

        val statsborgerskapListe = statsborgerskap.map { hentLand(it.land) }.joinToString(", ")

        val sivilstand: Sivilstand = sivilstand.firstOrNull() ?: Sivilstand(type = Sivilstandstype.UOPPGITT)

        return Person(
            fnr = EksternBrukerUtils.hentFnrFraToken(),
            alder = this.fødselsdato.kalkulerAlder(),
            forkortetNavn = navn.first().visningsnavn(),
            adresse = adresse,
            egenansatt = false, // TODO denne er vel i beste fall unødvendig?
            sivilstand = sivilstand.type.name,
            statsborgerskap = statsborgerskapListe,
            erStrengtFortrolig = adressebeskyttelse.erStrengtFortrolig(),
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
                vegadresse.bruksenhetsnummer ?: "",
            ),
        ) ?: ""

    private fun join(
        vararg args: String?,
        separator: String = ", ",
    ): String? {
        val filterNotNull = args.filterNotNull().filterNot(String::isEmpty)
        return if (filterNotNull.isEmpty()) {
            null
        } else {
            filterNotNull.joinToString(separator)
        }
    }

    private fun space(vararg args: String?): String? = join(*args, separator = " ")
}

private fun List<Fødselsdato>.kalkulerAlder(): Int {
    return this.first().fødselsdato?.let {
        return Period.between(it, LocalDate.now()).years
    } ?: error("Ingen fødselsdato registrert")
}

fun PdlAnnenForelder.tilDto(annenForelderPersonsIdent: String): Medforelder {
    val alder = this.fødselsdato.kalkulerAlder()
    if (this.adressebeskyttelse.harBeskyttetAdresse()) {
        return Medforelder(
            harAdressesperre = true,
            alder = alder,
        )
    }
    val annenForelderNavn = this.navn.first()
    return Medforelder(
        annenForelderNavn.visningsnavn(),
        this.adressebeskyttelse.harBeskyttetAdresse(),
        this.dødsfall.any(),
        annenForelderPersonsIdent,
        alder,
    )
}

fun List<Adressebeskyttelse>.harBeskyttetAdresse(): Boolean = kreverAdressebeskyttelse.contains(this.firstOrNull()?.gradering)

fun List<Adressebeskyttelse>.erStrengtFortrolig(): Boolean =
    this.firstOrNull()?.gradering?.let {
        AdressebeskyttelseGradering.STRENGT_FORTROLIG == it ||
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND == it
    } ?: false

private val kreverAdressebeskyttelse =
    listOf(
        AdressebeskyttelseGradering.FORTROLIG,
        AdressebeskyttelseGradering.STRENGT_FORTROLIG,
        AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
    )

data class BarnNavnOgIdent(
    val ident: String = "",
    val navn: String = "",
)
