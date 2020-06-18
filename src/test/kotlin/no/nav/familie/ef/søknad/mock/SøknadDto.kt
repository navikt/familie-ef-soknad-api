package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Adresse
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Søker
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.kontrakter.felles.objectMapper
import java.io.File

fun søknadDto(): SøknadDto = objectMapper.readValue(File("src/test/resources/søknadDto.json"), SøknadDto::class.java)

fun søkerMedDefaultVerdier(forventetFnr: String = "19128449828",
                           forkortetNavn: String = "Kari Nordmann",
                           adresse: Adresse = Adresse("Jerpefaret 5C", "1440"),
                           statsborgerskap: String = "Norsk",
                           telefonnummer: String? = "12345678",
                           sivilstatus: String = "Ugift") = Søker(fnr = forventetFnr,
                                                                  forkortetNavn = forkortetNavn,
                                                                  adresse = adresse,
                                                                  statsborgerskap = statsborgerskap,
                                                                  kontakttelefon = telefonnummer,
                                                                  sivilstand = sivilstatus,
                                                                  egenansatt = false

)

