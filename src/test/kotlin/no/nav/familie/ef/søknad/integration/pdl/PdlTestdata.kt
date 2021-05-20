package no.nav.familie.ef.søknad.integration.pdl

import no.nav.familie.ef.søknad.integration.dto.pdl.Adressebeskyttelse
import no.nav.familie.ef.søknad.integration.dto.pdl.AdressebeskyttelseGradering.UGRADERT
import no.nav.familie.ef.søknad.integration.dto.pdl.Bostedsadresse
import no.nav.familie.ef.søknad.integration.dto.pdl.BostedsadresseBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.DeltBosted
import no.nav.familie.ef.søknad.integration.dto.pdl.Dødsfall
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle.BARN
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle.FAR
import no.nav.familie.ef.søknad.integration.dto.pdl.ForelderBarnRelasjon
import no.nav.familie.ef.søknad.integration.dto.pdl.Fødsel
import no.nav.familie.ef.søknad.integration.dto.pdl.Matrikkeladresse
import no.nav.familie.ef.søknad.integration.dto.pdl.MatrikkeladresseBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.Navn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlSøker
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlSøkerData
import no.nav.familie.ef.søknad.integration.dto.pdl.PersonBolk
import no.nav.familie.ef.søknad.integration.dto.pdl.PersonDataBolk
import no.nav.familie.ef.søknad.integration.dto.pdl.Sivilstand
import no.nav.familie.ef.søknad.integration.dto.pdl.Sivilstandstype
import no.nav.familie.ef.søknad.integration.dto.pdl.Statsborgerskap
import no.nav.familie.ef.søknad.integration.dto.pdl.Vegadresse
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

    private val forelderBarnRelasjon = listOf(ForelderBarnRelasjon("", BARN))
    private val barnsRelasjoner = listOf(ForelderBarnRelasjon("", FAR))

    private val statsborgerskap = listOf(Statsborgerskap("", LocalDate.now(), LocalDate.now()))


    val pdlSøkerData =
            PdlSøkerData(PdlSøker(listOf(Adressebeskyttelse(UGRADERT)),
                                  bostedsadresse,
                                  forelderBarnRelasjon,
                                  navn,
                                  listOf(Sivilstand(Sivilstandstype.GIFT)),
                                  statsborgerskap))

    val pdlBarnData =
            PersonBolk(listOf(PersonDataBolk("11111122222",
                                             "ok",
                                             PdlBarn(adressebeskyttelse = listOf(Adressebeskyttelse(UGRADERT)),
                                                     bostedsadresse = bostedsadresseBarn,
                                                     deltBosted = listOf(DeltBosted(LocalDate.now(),
                                                                                    LocalDate.now())),
                                                     navn = navn,
                                                     fødsel = listOf(Fødsel(1, LocalDate.now())),
                                                     dødsfall = listOf(Dødsfall(LocalDate.now())),
                                                     forelderBarnRelasjon = barnsRelasjoner
                                             )
            )))


}
