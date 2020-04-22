package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn
import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDate

object BarnMapper {
    fun mapFolkeregistrerteBarn(barn: List<Barn>): List<RegistrertBarn> {
         return barn.filterNot{ falseOrNull(it.lagtTil) }
                .map { RegistrertBarn(navn = Søknadsfelt(it.navn.label, it.navn.verdi),
                                      fødselsnummer = Søknadsfelt(it.fnr.label, Fødselsnummer(it.fnr.verdi)),
                                      harSammeAdresse = Søknadsfelt(it.harSammeAdresse.label, it.harSammeAdresse.verdi),
                                      annenForelder = Søknadsfelt("Barnets andre forelder",
                                                    AnnenForelder()),

//                                                  AnnenForelder(person = Søknadsfelt("personalia",
//                                                                                     personMinimum()),
//                                                                adresse = adresseSøknadsfelt())),
                                      samvær = Søknadsfelt("samvær",
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
                                                                     "Vi sees stadig vekk")))) }


    }

    private fun falseOrNull(it: Boolean?) = it ?: false

    fun mapNyttBarn(barn: List<Barn>?): List<NyttBarn> {
        return listOf(nyttBarn())
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

    private fun personMinimum(): PersonMinimum {
        return PersonMinimum(Søknadsfelt("Navn", "Bob Burger"),
                             null,
                             Søknadsfelt("Fødselsdato", LocalDate.of(1992, 2, 18)))
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





    fun dokumentfelt(tittel: String) = Søknadsfelt("Dokument", Dokument(byteArrayOf(12), tittel))

}
