package no.nav.familie.ef.søknad.integration.dto.pdl

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalDateTime

data class PdlResponse<T>(val data: T?,
                          val errors: List<PdlError>?) {

    fun harFeil(): Boolean {
        return errors != null && errors.isNotEmpty()
    }

    fun errorMessages(): String {
        return errors?.joinToString { it -> it.message } ?: ""
    }
}

data class PdlError(val message: String)

data class PdlSøkerData(val person: PdlSøker)

data class PersonDataBolk<T>(val ident: String, val code: String, val person: T?)
data class PersonBolk<T>(val personBolk: List<PersonDataBolk<T>>)
data class PdlBolkResponse<T>(val data: PersonBolk<T>)

data class PdlSøker(val adressebeskyttelse: List<Adressebeskyttelse>,
                    val bostedsadresse: List<Bostedsadresse>,
                    val familierelasjoner: List<Familierelasjon>,
                    val folkeregisterpersonstatus: List<Folkeregisterpersonstatus>,
                    val navn: List<Navn>,
                    val sivilstand: List<Sivilstand>,
                    val statsborgerskap: List<Statsborgerskap>,
                    val tilrettelagtKommunikasjon: List<TilrettelagtKommunikasjon>)

data class PdlBarn(val adressebeskyttelse: List<Adressebeskyttelse>,
                   val bostedsadresse: List<Bostedsadresse>,
                   val deltBosted: List<DeltBosted>,
                   val familierelasjoner: List<Familierelasjon>,
                   val navn: List<Navn>,
                   @JsonProperty("foedsel") val fødsel: List<Fødsel>)

data class DeltBosted(val startdatoForKontrakt: LocalDate,
                      val sluttdatoForKontrakt: LocalDate?)

data class Folkeregistermetadata(val gyldighetstidspunkt: LocalDateTime?,
                                 @JsonProperty("opphoerstidspunkt") val opphørstidspunkt: LocalDateTime?)

data class Bostedsadresse(val angittFlyttedato: LocalDate?,
                          val coAdressenavn: String?,
                          val vegadresse: Vegadresse?,
                          val matrikkeladresse: Matrikkeladresse?)

data class Vegadresse(val husnummer: String?,
                      val husbokstav: String?,
                      val bruksenhetsnummer: String?,
                      val adressenavn: String?,
                      val kommunenummer: String?,
                      val tilleggsnavn: String?,
                      val postnummer: String?,
                      val matrikkelId: Long?)

data class Matrikkeladresse(val bruksenhetsnummer: String?,
                            val kommunenummer: String?,
                            val matrikkelId: String?,
                            val postnummer: String?,
                            val tilleggsnavn: String?)

data class Adressebeskyttelse(val gradering: AdressebeskyttelseGradering)

@Suppress("unused")
enum class AdressebeskyttelseGradering {

    STRENGT_FORTROLIG,
    STRENGT_FORTROLIG_UTLAND,
    FORTROLIG,
    UGRADERT
}

data class Fødsel(@JsonProperty("foedselsaar") val fødselsår: Int?,
                  @JsonProperty("foedselsdato") val fødselsdato: LocalDate?,
                  @JsonProperty("foedeland") val fødeland: String?,
                  @JsonProperty("foedested") val fødested: String?,
                  @JsonProperty("foedekommune") val fødekommune: String?)

data class Familierelasjon(val relatertPersonsIdent: String,
                           val relatertPersonsRolle: Familierelasjonsrolle,
                           val minRolleForPerson: Familierelasjonsrolle?)

enum class Familierelasjonsrolle {
    BARN,
    MOR,
    FAR,
    MEDMOR
}

data class Folkeregisterpersonstatus(val status: String,
                                     val forenkletStatus: String)

data class Navn(val fornavn: String,
                val mellomnavn: String?,
                val etternavn: String)

data class TilrettelagtKommunikasjon(@JsonProperty("talespraaktolk") val talespråktolk: Tolk?,
                                     @JsonProperty("tegnspraaktolk") val tegnspråktolk: Tolk?)

data class Tolk(@JsonProperty("spraak") val språk: String?)

data class Statsborgerskap(val land: String,
                           val gyldigFraOgMed: LocalDate?,
                           val gyldigTilOgMed: LocalDate?)


data class Sivilstand(val type: Sivilstandstype,
                      val gyldigFraOgMed: LocalDate?,
                      val myndighet: String?,
                      val kommune: String?,
                      val sted: String?,
                      val utland: String?,
                      val relatertVedSivilstand: String?,
                      val bekreftelsesdato: String?)

@Suppress("unused")
enum class Sivilstandstype {

    UOPPGITT,
    UGIFT,
    GIFT,
    ENKE_ELLER_ENKEMANN,
    SKILT,
    SEPARERT,
    REGISTRERT_PARTNER,
    SEPARERT_PARTNER,
    SKILT_PARTNER,
    GJENLEVENDE_PARTNER
}
