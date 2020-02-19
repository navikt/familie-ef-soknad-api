package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.*
import no.nav.familie.kontrakter.felles.objectMapper
import java.io.File
import java.time.LocalDate

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
                                                                  telefonnummer = telefonnummer,
                                                                  sivilstand = sivilstatus,
                                                                  egenansatt = false

)

fun sivilstatusMedDefaultVerdier(søkerHarSøktSeparasjon: BooleanFelt = BooleanFelt("Har dere søkt om separasjon, søkt om skilsmisse eller reist sak for domstolen?",
                                                                                   true),
                                 datoSøktSeparasjon: DatoFelt = DatoFelt("Når søkte dere eller reiste sak?",
                                                                         LocalDate.of(2015, 12, 23)),
                                 søkerGiftIUtlandet: BooleanFelt = BooleanFelt("Er du gift uten at dette er formelt registrert eller godkjent i Norge?",
                                                                               true),
                                 søkerSeparertEllerSkiltIUtlandet: BooleanFelt = BooleanFelt("Er du separert eller skilt uten at dette er formelt registrert eller godkjent i Norge?",
                                                                                             true),
                                 begrunnelseForSøknad: TekstFelt = TekstFelt("Hva er grunnen til at du er alene med barn?",
                                                                             "Endring i samværsordning"),
                                 datoForSamlivsbrudd: DatoFelt = DatoFelt("Dato for samlivsbrudd", LocalDate.of(2014, 10, 3)),
                                 datoFlyttetFraHverandre: DatoFelt = DatoFelt("Når flyttet dere fra hverandre?",
                                                                              LocalDate.of(2014, 10, 4)),
                                 datoEndretSamvær: DatoFelt = DatoFelt("Når skjedde endringen / når skal endringen skje?",
                                                                       LocalDate.of(2013, 4, 17)),
                                 begrunnelseAnnet: TekstFelt = TekstFelt("Spesifiser grunnen til at du er alene med barn?",
                                                                         "Trives best alene")) = Sivilstatus(
        søkerHarSøktSeparasjon = søkerHarSøktSeparasjon,
        datoSøktSeparasjon = datoSøktSeparasjon,
        søkerGiftIUtlandet = søkerGiftIUtlandet,
        søkerSeparertEllerSkiltIUtlandet = søkerSeparertEllerSkiltIUtlandet,
        begrunnelseForSøknad = begrunnelseForSøknad,
        datoForSamlivsbrudd = datoForSamlivsbrudd,
        datoFlyttetFraHverandre = datoFlyttetFraHverandre,
        datoEndretSamvær = datoEndretSamvær,
        begrunnelseAnnet = begrunnelseAnnet)

fun vedleggListeMock() = listOf(VedleggFelt("DOKUMENTID123", "samlivsbrudd", "Erklæring om samlivsbrudd"))