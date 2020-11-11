package no.nav.familie.ef.søknad.integration.pdl

import no.nav.familie.ef.søknad.integration.dto.pdl.*
import java.time.LocalDate

object PdlTestdata {

    private val vegadresse = Vegadresse("",
                                        "",
                                        "",
                                        "",
                                        "",
                                        0L)


    private val navn = listOf(Navn("", "", ""))

    private val adressebeskyttelse = listOf(Adressebeskyttelse(AdressebeskyttelseGradering.FORTROLIG))

    private val matrikkeladresse = Matrikkeladresse(null)

    private val bostedsadresse = listOf(Bostedsadresse(LocalDate.now(),
                                                       "",
                                                       vegadresse,
                                                       matrikkeladresse))

    private val familierelasjon = listOf(Familierelasjon("", Familierelasjonsrolle.BARN, Familierelasjonsrolle.FAR))

    private val statsborgerskap = listOf(Statsborgerskap("", LocalDate.now(), LocalDate.now()))

    private val fødsel = listOf(Fødsel(1, LocalDate.now(), "", "", ""))


    val pdlSøkerData =
            PdlSøkerData(PdlSøker(adressebeskyttelse,
                                  bostedsadresse,
                                  familierelasjon,
                                  listOf(Folkeregisterpersonstatus("", "")),
                                  navn,
                                  listOf(Sivilstand(Sivilstandstype.GIFT, LocalDate.now(), "", "", "", "", "", "")),
                                  statsborgerskap,
                                  listOf(TilrettelagtKommunikasjon(Tolk(""), Tolk("")))))

    val pdlBarnData =
            PersonBolk(listOf(PersonDataBolk("11111122222", "ok", PdlBarn(adressebeskyttelse,
                                                                          bostedsadresse,
                                                                          listOf(DeltBosted(LocalDate.now(),
                                                                                            LocalDate.now())),
                                                                          familierelasjon,
                                                                          navn,
                                                                          fødsel))))


}
