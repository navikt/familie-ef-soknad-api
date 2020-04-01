package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidsforhold
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Firma
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.kontrakter.ef.søknad.*
import java.time.Month
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker as ArbeidssøkerDto

object AktivitetsMapper {
    fun map(frontendDto: SøknadDto): Aktivitet {
        val aktivitet = frontendDto.aktivitet
        return Aktivitet(hvordanErArbeidssituasjonen = Søknadsfelt(aktivitet.hvaErDinArbeidssituasjon.label,
                                                                   aktivitet.hvaErDinArbeidssituasjon.verdi),
                         arbeidsforhold = aktivitet.arbeidsforhold?.let {
                             Søknadsfelt("Om arbeidsforholdet ditt",
                                         mapArbeidsforhold(aktivitet.arbeidsforhold))
                         },
                         selvstendig = aktivitet.firma?.let { Søknadsfelt("Om firmaet du driver", mapOmFirma(it)) },
                         virksomhet = aktivitet.etablererEgenVirksomhet?.let { mapEtablererVirksomhet(it) },
                         arbeidssøker = aktivitet.arbeidssøker.let { mapArbeidssøker(it) },
                         underUtdanning = Søknadsfelt("Utdanningen du skal ta",
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
                                                                     Søknadsfelt("Har du tatt utdanning etter grunnskolen?",
                                                                                 true),
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

    private fun mapArbeidssøker(arbeidssøker: ArbeidssøkerDto): Søknadsfelt<Arbeidssøker> {
        return Søknadsfelt("Når du er arbeidssøker",
                           Arbeidssøker(registrertSomArbeidssøkerNav = Søknadsfelt(arbeidssøker.registrertSomArbeidssøkerNav.label,
                                                                                   arbeidssøker.registrertSomArbeidssøkerNav.verdi),
                                        villigTilÅTaImotTilbudOmArbeid = Søknadsfelt(arbeidssøker.villigTilÅTaImotTilbudOmArbeid.label,
                                                                                     arbeidssøker.villigTilÅTaImotTilbudOmArbeid.verdi),
                                        kanDuBegynneInnenEnUke = Søknadsfelt(arbeidssøker.kanBegynneInnenEnUke.label,
                                                                             arbeidssøker.kanBegynneInnenEnUke.verdi),
                                        kanDuSkaffeBarnepassInnenEnUke = Søknadsfelt(arbeidssøker.kanSkaffeBarnepassInnenEnUke.label,
                                                                                     arbeidssøker.kanSkaffeBarnepassInnenEnUke.verdi),
                                        hvorØnskerDuArbeid = Søknadsfelt(arbeidssøker.hvorØnskerSøkerArbeid.label,
                                                                         arbeidssøker.hvorØnskerSøkerArbeid.verdi),
                                        ønskerDuMinst50ProsentStilling = Søknadsfelt(arbeidssøker.ønskerSøker50ProsentStilling.label,
                                                                                     arbeidssøker.ønskerSøker50ProsentStilling.verdi)))
    }

    private fun mapEtablererVirksomhet(it: TekstFelt): Søknadsfelt<Virksomhet> {
        return Søknadsfelt("Om virksomheten du etablerer",
                           Virksomhet(Søknadsfelt(it.label,
                                                  it.verdi)))
    }

    private fun mapOmFirma(firma: Firma): Selvstendig {
        return Selvstendig(firmanavn = Søknadsfelt(firma.navn.label, firma.navn.verdi),
                           organisasjonsnummer = Søknadsfelt(firma.organisasjonsnummer.label, firma.organisasjonsnummer.verdi),
                           etableringsdato = Søknadsfelt(firma.etableringsdato.label, firma.etableringsdato.verdi),
                           arbeidsmengde = Søknadsfelt(firma.arbeidsmengde.label, firma.arbeidsmengde.verdi.toInt()),
                           hvordanSerArbeidsukenUt = Søknadsfelt(firma.arbeidsuke.label, firma.arbeidsuke.verdi))
    }

    private fun mapArbeidsforhold(arbeidsforhold: List<Arbeidsforhold>): List<Arbeidsgiver> {

        return arbeidsforhold.map { arbeid ->
            Arbeidsgiver(arbeidsgivernavn = Søknadsfelt(arbeid.navn.label, arbeid.navn.verdi),
                         stillingsprosent = Søknadsfelt(arbeid.arbeidsmengde.label, arbeid.arbeidsmengde.verdi.toInt()),
                         fastEllerMidlertidig = Søknadsfelt(arbeid.fastStilling.label, arbeid.fastStilling.verdi),
                         harDuEnSluttdato = Søknadsfelt(arbeid.harSluttDato.label, arbeid.harSluttDato.verdi),
                         sluttdato = Søknadsfelt(arbeid.sluttdato.label, arbeid.sluttdato.verdi))
        }


    }


}
