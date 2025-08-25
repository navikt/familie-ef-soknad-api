package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.søknad.domain.Adresse
import no.nav.familie.ef.søknad.søknad.domain.Søker
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadSkolepengerDto
import no.nav.familie.kontrakter.felles.objectMapper
import java.io.File

fun søknadOvergangsstønadDto(): SøknadOvergangsstønadDto = objectMapper.readValue(File("src/test/resources/søknadDto.json"), SøknadOvergangsstønadDto::class.java)

fun søknadBarnetilsynDto(): SøknadBarnetilsynDto = objectMapper.readValue(File("src/test/resources/barnetilsyn/BarnetilsynsøknadDto.json"), SøknadBarnetilsynDto::class.java)

fun søknadSkolepengerDto(): SøknadSkolepengerDto = objectMapper.readValue(File("src/test/resources/skolepenger/skolepenger.json"), SøknadSkolepengerDto::class.java)

fun søkerMedDefaultVerdier(
    forventetFnr: String = "19128449828",
    forkortetNavn: String = "Kari Nordmann",
    adresse: Adresse = Adresse("Jerpefaret 5C", "1440", ""),
    statsborgerskap: String = "Norsk",
    sivilstatus: String = "Ugift",
) = Søker(
    fnr = forventetFnr,
    forkortetNavn = forkortetNavn,
    adresse = adresse,
    statsborgerskap = statsborgerskap,
    sivilstand = sivilstatus,
    egenansatt = false,
)
