package no.nav.familie.ef.søknad.integration.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class PersoninfoDto(
    val ident: String,
    val navn: NavnDto,
    val adresseinfo: AdresseinfoDto?,
    @JsonProperty("doedsdato") val dødsdato: DødsdatoDto?,
    val egenansatt: EgenansattDto?,
    val foreldreansvar: KodeMedDatoOgKildeDto?,
    val innvandringUtvandring: InnvandringUtvandringDto?,
    val kontonummer: KontonummerDto?,
    val oppholdstillatelse: KodeMedDatoOgKildeDto?,
    val sivilstand: KodeMedDatoOgKildeDto?,
    @JsonProperty("spraak") val språk: KodeMedDatoOgKildeDto?,
    val statsborgerskap: KodeMedDatoOgKildeDto?,
    val telefon: TelefoninfoDto?,
)

data class NavnDto(val forkortetNavn: String)

data class AdresseinfoDto(@JsonProperty("boadresse") val bostedsadresse: BostedsadresseDto?)

data class BostedsadresseDto(
    val adresse: String?,
    val adressetillegg: String?,
    val kommune: String?,
    val landkode: String?,
    val postnummer: String?,
)

data class DødsdatoDto(val dato: LocalDate?)

data class EgenansattDto(
    val datoFraOgMed: LocalDate?,
    val isErEgenansatt: Boolean,
)

class InnvandringUtvandringDto(
    val innvandretDato: LocalDate?,
    val utvandretDato: LocalDate?,
)

class TelefoninfoDto(
    val jobb: String?,
    val mobil: String?,
    val privat: String?,
)

data class KontonummerDto(val nummer: String?)
