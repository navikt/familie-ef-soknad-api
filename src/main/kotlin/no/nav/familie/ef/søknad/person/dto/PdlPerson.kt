package no.nav.familie.ef.søknad.person.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class PdlResponse<T>(
    val data: T,
    val errors: List<PdlError>?,
    val extensions: PdlExtensions?,
) {
    fun harFeil(): Boolean = errors != null && errors.isNotEmpty()

    fun harAdvarsel(): Boolean = !extensions?.warnings.isNullOrEmpty()

    fun errorMessages(): String = errors?.joinToString { it -> it.message } ?: ""
}

data class PdlExtensions(
    val warnings: List<PdlWarning>?,
)

data class PdlWarning(
    val details: Any?,
    val id: String?,
    val message: String?,
    val query: String?,
)

data class PdlError(
    val message: String,
    val extensions: PdlErrorExtensions?,
)

data class PdlErrorExtensions(
    val code: String?,
) {
    fun notFound() = code == "not_found"
}

data class PdlSøkerData(
    val person: PdlSøker?,
)

data class PersonDataBolk<T>(
    val ident: String,
    val code: String,
    val person: T?,
)

data class PersonBolk<T>(
    val personBolk: List<PersonDataBolk<T>>,
)

data class PdlBolkResponse<T>(
    val data: PersonBolk<T>?,
    val errors: List<PdlError>?,
    val extensions: PdlExtensions?,
) {
    fun errorMessages(): String = errors?.joinToString { it -> it.message } ?: ""

    fun harAdvarsel(): Boolean = !extensions?.warnings.isNullOrEmpty()
}

data class PdlSøker(
    val adressebeskyttelse: List<Adressebeskyttelse>,
    val bostedsadresse: List<Bostedsadresse>,
    val forelderBarnRelasjon: List<ForelderBarnRelasjon>,
    val navn: List<Navn>,
    val sivilstand: List<Sivilstand>,
    val statsborgerskap: List<Statsborgerskap>,
    @JsonProperty("foedselsdato") val fødselsdato: List<Fødselsdato>,
)

data class PdlBarn(
    val adressebeskyttelse: List<Adressebeskyttelse>,
    val bostedsadresse: List<BostedsadresseBarn>,
    val deltBosted: List<DeltBosted>,
    val navn: List<Navn>,
    @JsonProperty("foedselsdato") val fødselsdato: List<Fødselsdato>,
    @JsonProperty("doedsfall") val dødsfall: List<Dødsfall>,
    val forelderBarnRelasjon: List<ForelderBarnRelasjon>,
)

data class PdlAnnenForelder(
    val adressebeskyttelse: List<Adressebeskyttelse>,
    val navn: List<Navn>,
    @JsonProperty("doedsfall") val dødsfall: List<Dødsfall>,
    @JsonProperty("foedselsdato") val fødselsdato: List<Fødselsdato>,
)

data class Adressebeskyttelse(
    val gradering: AdressebeskyttelseGradering,
)

/**
 * Fra pdl dok 2021-01-14:
 * STRENGT_FORTROLIG, Tidligere spesregkode kode 6 fra TPS
 * STRENGT_FORTROLIG_UTLAND,  Fra pdl dok: Tilsvarer paragraf 19 i Bisys (henvisning til Forvaltningslovens §19).
 * Ved strengt fortrolig utland behandles personen i NAV tilsvarende som ved graderingen strengt fortrolig fra Folkeregisteret.
 * FORTROLIG,  Tidligere spesregkode kode 7 fra TPS.
 * UGRADERT, Kode vi kan få fra Folkeregisteret. Vi har ingen tilfeller per i dag i produksjon.
 */
enum class AdressebeskyttelseGradering {
    STRENGT_FORTROLIG,
    STRENGT_FORTROLIG_UTLAND,
    FORTROLIG,
    UGRADERT,
}

data class DeltBosted(
    val startdatoForKontrakt: LocalDate,
    val sluttdatoForKontrakt: LocalDate?,
)

data class Bostedsadresse(
    val vegadresse: Vegadresse?,
    val matrikkeladresse: Matrikkeladresse?,
)

data class BostedsadresseBarn(
    val vegadresse: Vegadresse?,
    val matrikkeladresse: MatrikkeladresseBarn?,
)

data class Vegadresse(
    val husnummer: String?,
    val husbokstav: String?,
    val bruksenhetsnummer: String?,
    val adressenavn: String?,
    val postnummer: String?,
    val matrikkelId: Long?,
)

interface MatrikkelId {
    val matrikkelId: Long?
}

data class Matrikkeladresse(
    override val matrikkelId: Long?,
    val tilleggsnavn: String?,
    val postnummer: String?,
) : MatrikkelId

data class MatrikkeladresseBarn(
    override val matrikkelId: Long?,
) : MatrikkelId

data class Fødselsdato(
    @JsonProperty("foedselsaar") val fødselsår: Int?,
    @JsonProperty("foedselsdato") val fødselsdato: LocalDate?,
)

data class Dødsfall(
    @JsonProperty("doedsdato") val dødsdato: LocalDate?,
)

data class ForelderBarnRelasjon(
    val relatertPersonsIdent: String?,
    val relatertPersonsRolle: Familierelasjonsrolle,
)

enum class Familierelasjonsrolle {
    BARN,
    MOR,
    FAR,
    MEDMOR,
}

data class Navn(
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String,
)

data class Statsborgerskap(
    val land: String,
    val gyldigFraOgMed: LocalDate?,
    val gyldigTilOgMed: LocalDate?,
)

data class Sivilstand(
    val type: Sivilstandstype,
)

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
    GJENLEVENDE_PARTNER,
}
