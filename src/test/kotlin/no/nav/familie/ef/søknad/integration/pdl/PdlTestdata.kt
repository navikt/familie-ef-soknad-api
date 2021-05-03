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


    private val matrikkeladresse = Matrikkeladresse(null, null, null)
    private val matrikkeladresseBarn = MatrikkeladresseBarn(null)

    private val bostedsadresse = listOf(Bostedsadresse(vegadresse,
                                                       matrikkeladresse))

    private val bostedsadresseBarn = listOf(BostedsadresseBarn(vegadresse,
                                                               matrikkeladresseBarn))

    private val forelderBarnRelasjon = listOf(ForelderBarnRelasjon("", Familierelasjonsrolle.BARN))

    private val statsborgerskap = listOf(Statsborgerskap("", LocalDate.now(), LocalDate.now()))


    val pdlSøkerData =
            PdlSøkerData(PdlSøker(listOf(Adressebeskyttelse(AdressebeskyttelseGradering.UGRADERT)),
                                  bostedsadresse,
                                  forelderBarnRelasjon,
                                  navn,
                                  listOf(Sivilstand(Sivilstandstype.GIFT)),
                                  statsborgerskap))

    val pdlBarnData =
            PersonBolk(listOf(PersonDataBolk("11111122222",
                                             "ok",
                                             PdlBarn(listOf(Adressebeskyttelse(AdressebeskyttelseGradering.UGRADERT)),
                                                     bostedsadresseBarn,
                                                     listOf(DeltBosted(LocalDate.now(),
                                                                       LocalDate.now())),
                                                     navn,
                                                     listOf(Fødsel(1, LocalDate.now())),
                                                     listOf(Dødsfall(LocalDate.now())
                                                     )))))


}
