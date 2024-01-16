package no.nav.familie.ef.søknad.person

import no.nav.familie.ef.søknad.person.dto.Adressebeskyttelse
import no.nav.familie.ef.søknad.person.dto.AdressebeskyttelseGradering.UGRADERT
import no.nav.familie.ef.søknad.person.dto.Bostedsadresse
import no.nav.familie.ef.søknad.person.dto.BostedsadresseBarn
import no.nav.familie.ef.søknad.person.dto.DeltBosted
import no.nav.familie.ef.søknad.person.dto.Dødsfall
import no.nav.familie.ef.søknad.person.dto.Familierelasjonsrolle.BARN
import no.nav.familie.ef.søknad.person.dto.Familierelasjonsrolle.FAR
import no.nav.familie.ef.søknad.person.dto.ForelderBarnRelasjon
import no.nav.familie.ef.søknad.person.dto.Fødsel
import no.nav.familie.ef.søknad.person.dto.Matrikkeladresse
import no.nav.familie.ef.søknad.person.dto.MatrikkeladresseBarn
import no.nav.familie.ef.søknad.person.dto.Navn
import no.nav.familie.ef.søknad.person.dto.PdlBarn
import no.nav.familie.ef.søknad.person.dto.PdlSøker
import no.nav.familie.ef.søknad.person.dto.PdlSøkerData
import no.nav.familie.ef.søknad.person.dto.PersonBolk
import no.nav.familie.ef.søknad.person.dto.PersonDataBolk
import no.nav.familie.ef.søknad.person.dto.Sivilstand
import no.nav.familie.ef.søknad.person.dto.Sivilstandstype
import no.nav.familie.ef.søknad.person.dto.Statsborgerskap
import no.nav.familie.ef.søknad.person.dto.Vegadresse
import java.time.LocalDate

object PdlTestdata {

    private val vegadresse = Vegadresse(
        "",
        "",
        "",
        "",
        "",
        0L,
    )

    private val navn = listOf(Navn("", "", ""))

    private val matrikkeladresse = Matrikkeladresse(null, null, null)
    private val matrikkeladresseBarn = MatrikkeladresseBarn(null)

    private val bostedsadresse = listOf(
        Bostedsadresse(
            vegadresse,
            matrikkeladresse,
        ),
    )

    private val bostedsadresseBarn = listOf(
        BostedsadresseBarn(
            vegadresse,
            matrikkeladresseBarn,
        ),
    )

    private val forelderBarnRelasjon = listOf(ForelderBarnRelasjon("", BARN))
    private val barnsRelasjoner = listOf(ForelderBarnRelasjon("", FAR))

    private val statsborgerskap = listOf(Statsborgerskap("", LocalDate.now(), LocalDate.now()))

    val pdlSøkerData =
        PdlSøkerData(
            PdlSøker(
                listOf(Adressebeskyttelse(UGRADERT)),
                bostedsadresse,
                forelderBarnRelasjon,
                navn,
                listOf(Sivilstand(Sivilstandstype.GIFT)),
                statsborgerskap,
            ),
        )

    val pdlBarnData =
        PersonBolk(
            listOf(
                PersonDataBolk(
                    "11111122222",
                    "ok",
                    PdlBarn(
                        adressebeskyttelse = listOf(Adressebeskyttelse(UGRADERT)),
                        bostedsadresse = bostedsadresseBarn,
                        deltBosted = listOf(
                            DeltBosted(
                                LocalDate.now(),
                                LocalDate.now(),
                            ),
                        ),
                        navn = navn,
                        fødsel = listOf(Fødsel(1, LocalDate.now())),
                        dødsfall = listOf(Dødsfall(LocalDate.now())),
                        forelderBarnRelasjon = barnsRelasjoner,
                    ),
                ),
            ),
        )
}
