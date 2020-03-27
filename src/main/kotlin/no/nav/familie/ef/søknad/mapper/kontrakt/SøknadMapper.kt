package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.VedleggFelt
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.*
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month

@Component
class SøknadMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(frontendDto: SøknadDto): Søknad {
        val dokumenter: Map<String, Dokument> = hentDokumenter(frontendDto.vedleggsliste)

        return Søknad(
                personalia = Søknadsfelt("Søker", PersonaliaMapper.mapPersonalia(frontendDto)),
                sivilstandsdetaljer = Søknadsfelt("Detaljer om sivilstand",
                                                  SivilstandsdetaljerMapper.mapSivilstandsdetaljer(frontendDto, dokumenter)),
                medlemskapsdetaljer = Søknadsfelt("Opphold i Norge", MedlemsskapsMapper.mapMedlemskap(frontendDto, dokumenter)),
                bosituasjon = Søknadsfelt("Bosituasjonen din", BosituasjonMapper.mapBosituasjon(frontendDto.bosituasjon)),
                sivilstandsplaner = Søknadsfelt("Sivilstandsplaner",
                                                SivilstandsplanerMapper.mapSivilstandsplaner(frontendDto.bosituasjon)),
                folkeregisterbarn = Søknadsfelt("Barn funnet i tps/folkeregisteret", listOf(registrertBarn())),
                kommendeBarn = Søknadsfelt("Barn lagt til", listOf(nyttBarn())),
                aktivitet = Søknadsfelt("Arbeid, utdanning og andre aktiviteter", aktivitet()),
                situasjon = Søknadsfelt("Mer om situasjonen din", SituasjonsMapper.mapSituasjon(frontendDto, dokumenter)),
                stønadsstart = Søknadsfelt("Når søker du stønad fra?", stønadsstart()))
    }

    private fun hentDokumenter(vedleggListe: List<VedleggFelt>): Map<String, Dokument> {
        return vedleggListe.associate { it.navn to Dokument(dokumentServiceService.hentVedlegg(it.dokumentId), it.label) }
    }

    private fun stønadsstart() = Stønadsstart(Søknadsfelt("Fra måned", Month.AUGUST), Søknadsfelt("Fra år", 2018))

    @Suppress("LongLine")
    private fun situasjon(): Situasjon {
        return Situasjon(Søknadsfelt("Gjelder noe av dette deg?",
                                     listOf("Barnet mitt er sykt",
                                            "Jeg har søkt om barnepass, men ikke fått plass enda",
                                            "Jeg har barn som har behov for særlig tilsyn på grunn av fysiske, psykiske eller store sosiale problemer")),
                         dokumentfelt("Legeerklæring"),
                         dokumentfelt("Legeattest for egen sykdom eller sykt barn"),
                         dokumentfelt("Avslag på søknad om barnehageplass, skolefritidsordning e.l."),
                         dokumentfelt("Dokumentasjon av særlig tilsynsbehov"),
                         dokumentfelt("Dokumentasjon av studieopptak"),
                         Søknadsfelt("Når skal du starte i ny jobb?", LocalDate.of(2045, 12, 16)),
                         dokumentfelt("Dokumentasjon av jobbtilbud"),
                         Søknadsfelt("Når skal du starte utdanningen?", LocalDate.of(2025, 7, 28)),
                         Søknadsfelt("Har du sagt opp jobben eller redusert arbeidstiden de siste 6 månedene?",
                                     "Ja, jeg har sagt opp jobben eller tatt frivillig permisjon (ikke foreldrepermisjon)"),
                         Søknadsfelt("Hvorfor sa du opp?", "Sjefen var dum"),
                         Søknadsfelt("Når sa du opp?", LocalDate.of(2014, 1, 12)),
                         dokumentfelt("Dokumentasjon av arbeidsforhold"))
    }

    @Suppress("LongLine")
    private fun aktivitet(): Aktivitet {
        return Aktivitet(Søknadsfelt("Hvordan er arbeidssituasjonen din?",
                                     listOf("Jeg er hjemme med barn under 1 år",
                                            "Jeg er i arbeid",
                                            "Jeg er selvstendig næringsdrivende eller frilanser")),
                         Søknadsfelt("Om arbeidsforholdet ditt",
                                     listOf(Arbeidsgiver(Søknadsfelt("Navn på arbeidsgiveren", "Palpatine"),
                                                         Søknadsfelt("Hvor mye jobber du?", 15),
                                                         Søknadsfelt("Er stillingen fast eller midlertidig?",
                                                                     "Fast"),
                                                         Søknadsfelt("Har du en sluttdato?", true),
                                                         Søknadsfelt("Når skal du slutte?",
                                                                     LocalDate.of(2020, 11, 18))))),
                         Søknadsfelt("Om firmaet du driver",
                                     Selvstendig(Søknadsfelt("Navn på firma", "Bobs burgers"),
                                                 Søknadsfelt("Organisasjonsnummer", "987654321"),
                                                 Søknadsfelt("Når etablerte du firmaet?",
                                                             LocalDate.of(2018, 4, 5)),
                                                 Søknadsfelt("Hvor mye jobber du?", 150),
                                                 Søknadsfelt("Hvordan ser arbeidsuken din ut?",
                                                             "Veldig tung"))),
                         Søknadsfelt("Om virksomheten du etablerer",
                                     Virksomhet(Søknadsfelt("Beskriv virksomheten",
                                                            "Den kommer til å revolusjonere verden"))),
                         Søknadsfelt("Når du er arbeidssøker",
                                     Arbeidssøker(Søknadsfelt("Er du registrert som arbeidssøker hos NAV?", true),
                                                  Søknadsfelt("Er du villig til å ta imot tilbud om arbeid eller arbeidsmarkedstiltak?",
                                                              true),
                                                  Søknadsfelt("Kan du begynne i arbeid senest én uke etter at du har fått tilbud om jobb?",
                                                              true),
                                                  Søknadsfelt("Har du eller kan du skaffe barnepass senest innen en uke etter at du har fått tilbud om jobb eller arbeidsmarkedstiltak?",
                                                              false),
                                                  Søknadsfelt("Hvor ønsker du å søke arbeid?",
                                                              "Kun i bodistriktet mitt, ikke mer enn 1 times reisevei"),
                                                  Søknadsfelt("Ønsker du å stå som arbeidssøker til minst 50% stilling?",
                                                              true))),
                         Søknadsfelt("Utdanningen du skal ta",
                                     UnderUtdanning(Søknadsfelt("Skole/utdanningssted", "UiO"),
                                                    Søknadsfelt("Utdanning",
                                                                Utdanning(Søknadsfelt("Linje/kurs/grad",
                                                                                      "Profesjonsstudium Informatikk"),
                                                                          Søknadsfelt("Når skal du være elev/student?",
                                                                                      Periode(Month.JANUARY,
                                                                                              1999,
                                                                                              Month.OCTOBER,
                                                                                              2004))
                                                                )),
                                                    Søknadsfelt("Er utdanningen offentlig eller privat?",
                                                                "Offentlig"),
                                                    Søknadsfelt("Hvor mye skal du studere?", 300),
                                                    Søknadsfelt("Hva er målet med utdanningen?",
                                                                "Økonomisk selvstendighet"),
                                                    Søknadsfelt("Har du tatt utdanning etter grunnskolen?", true),
                                                    Søknadsfelt("Tidligere Utdanning",
                                                                listOf(Utdanning(Søknadsfelt("Linje/kurs/grad",
                                                                                             "Master Fysikk"),
                                                                                 Søknadsfelt("Når var du elev/student?",
                                                                                             Periode(Month.JANUARY,
                                                                                                     1999,
                                                                                                     Month.OCTOBER,
                                                                                                     2004))
                                                                ))))))
    }

    @Suppress("LongLine")
    private fun nyttBarn(): NyttBarn {
        return NyttBarn(navn = Søknadsfelt("Barnets fulle navn, hvis dette er bestemt", "Sorgløs"),
                        erBarnetFødt = Søknadsfelt("Er barnet født?", false),
                        fødselTermindato = Søknadsfelt("Termindato", LocalDate.of(2020, 5, 16)),
                        skalBarnetBoHosSøker = Søknadsfelt("Skal barnet bo hos deg?", true),
                        terminbekreftelse = dokumentfelt(
                                "Bekreftelse på ventet fødselsdato"),
                        annenForelder = Søknadsfelt("Barnets andre forelder",
                                                    AnnenForelder(Søknadsfelt("Jeg kan ikke oppgi den andre forelderen",
                                                                              true),
                                                                  Søknadsfelt("Hvorfor kan du ikke oppgi den andre forelderen?",
                                                                              "Fordi jeg ikke liker hen."))),
                        samvær = Søknadsfelt("Samvær",
                                             Samvær(Søknadsfelt("Har du og den andre forelderen skriftlig avtale om delt bosted for barnet?",
                                                                true),
                                                    dokumentfelt(
                                                            "Avtale om samvær"),
                                                    Søknadsfelt("Har den andre forelderen samvær med barnet",
                                                                "Ja, men ikke mer enn vanlig samværsrett"),
                                                    Søknadsfelt("Har dere skriftlig samværsavtale for barnet?",
                                                                "Ja, men den beskriver ikke når barnet er sammen med hver av foreldrene"),
                                                    dokumentfelt(
                                                            "Avtale om samvær"),
                                                    Søknadsfelt("Hvordan praktiserer dere samværet?",
                                                                "Litt hver for oss"),
                                                    Søknadsfelt("Bor du og den andre forelderen til [barnets navn] i samme hus/blokk, gårdstun, kvartal eller vei?",
                                                                true),
                                                    Søknadsfelt("Har du bodd sammen med den andre forelderen til [barnets fornavn] før?",
                                                                true),
                                                    Søknadsfelt("Når flyttet dere fra hverandre?",
                                                                LocalDate.of(2018, 7, 21)),
                                                    dokumentfelt(
                                                            "Erklæring om samlivsbrudd"),
                                                    Søknadsfelt("Hvor mye er du sammen med den andre forelderen til barnet?",
                                                                "Vi møtes også uten at barnet er til stede"),
                                                    Søknadsfelt("Beskriv  hvor mye er du sammen med den andre forelderen til barnet?",
                                                                "Vi sees stadig vekk"))))
    }

    @Suppress("LongLine")
    private fun registrertBarn(): RegistrertBarn {
        return RegistrertBarn(Søknadsfelt("Navn", "Lykkeliten"),
                              Søknadsfelt("Fødselsnummer", Fødselsnummer("31081953069")),
                              Søknadsfelt("Har samme adresse som søker", true),
                              Søknadsfelt("Barnets andre forelder",
                                          AnnenForelder(person = Søknadsfelt("personalia",
                                                                             personMinimum()),
                                                        adresse = adresseSøknadsfelt())),
                              Søknadsfelt("samvær",
                                          Samvær(Søknadsfelt("Har du og den andre forelderen skriftlig avtale om delt bosted for barnet?",
                                                             true),
                                                 dokumentfelt(
                                                         "Avtale om delt bosted for barna"),
                                                 Søknadsfelt("Har den andre forelderen samvær med barnet",
                                                             "Ja, men ikke mer enn vanlig samværsrett"),
                                                 Søknadsfelt("Har dere skriftlig samværsavtale for barnet?",
                                                             "Ja, men den beskriver ikke når barnet er sammen med hver av foreldrene"),
                                                 dokumentfelt(
                                                         "Avtale om samvær"),
                                                 Søknadsfelt("Hvordan praktiserer dere samværet?",
                                                             "Litt hver for oss"),
                                                 Søknadsfelt("Bor du og den andre forelderen til [barnets navn] i samme hus/blokk, gårdstun, kvartal eller vei?",
                                                             true),
                                                 Søknadsfelt("Har du bodd sammen med den andre forelderen til [barnets fornavn] før?",
                                                             true),
                                                 Søknadsfelt("Når flyttet dere fra hverandre?",
                                                             LocalDate.of(2018, 7, 21)),
                                                 dokumentfelt(
                                                         "Erklæring om samlivsbrudd"),
                                                 Søknadsfelt("Hvor mye er du sammen med den andre forelderen til barnet?",
                                                             "Vi møtes også uten at barnet er til stede"),
                                                 Søknadsfelt("Beskriv  hvor mye er du sammen med den andre forelderen til barnet?",
                                                             "Vi sees stadig vekk"))))
    }

    fun adresseSøknadsfelt(): Søknadsfelt<Adresse> {
        return Søknadsfelt("Adresse",
                           Adresse("Jerpefaret 5C",
                                   "1440",
                                   "Drøbak",
                                   "Norge"))
    }

    fun dokumentfelt(tittel: String) = Søknadsfelt("Dokument", Dokument(byteArrayOf(12), tittel))

    private fun personMinimum(): PersonMinimum {
        return PersonMinimum(Søknadsfelt("Navn", "Bob Burger"),
                             null,
                             Søknadsfelt("Fødselsdato", LocalDate.of(1992, 2, 18)))
    }
}
