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
                aktivitet = Søknadsfelt("Arbeid, utdanning og andre aktiviteter", AktivitetsMapper.map(frontendDto)),
                situasjon = Søknadsfelt("Mer om situasjonen din", SituasjonsMapper.mapSituasjon(frontendDto, dokumenter)),
                stønadsstart = Søknadsfelt("Når søker du stønad fra?", stønadsstart()))
    }

    private fun hentDokumenter(vedleggListe: List<VedleggFelt>): Map<String, Dokument> {
        return vedleggListe.associate { it.navn to Dokument(dokumentServiceService.hentVedlegg(it.dokumentId), it.label) }
    }

    private fun stønadsstart() = Stønadsstart(Søknadsfelt("Fra måned", Month.AUGUST), Søknadsfelt("Fra år", 2018))



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
