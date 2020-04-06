package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.*
import no.nav.familie.kontrakter.ef.søknad.*
import no.nav.familie.kontrakter.ef.søknad.Aktivitet
import no.nav.familie.kontrakter.ef.søknad.Periode
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker as ArbeidssøkerDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.UnderUtdanning as UnderUtdanningDto

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
                         underUtdanning = aktivitet.underUtdanning.let { mapUtdanning(it) })
    }

    private fun mapUtdanning(underUtdanning: UnderUtdanningDto): Søknadsfelt<UnderUtdanning> {
        return Søknadsfelt("Utdanningen du skal ta", mapUnderUtdanning(underUtdanning))

    }

    private fun mapUnderUtdanning(underUtdanning: no.nav.familie.ef.søknad.api.dto.søknadsdialog.UnderUtdanning): UnderUtdanning {
        return UnderUtdanning(skoleUtdanningssted = Søknadsfelt(underUtdanning.skoleUtdanningssted.label,
                                                                underUtdanning.skoleUtdanningssted.verdi),
                              utdanning = Søknadsfelt("Utdanning",
                                                      Utdanning(Søknadsfelt(underUtdanning.linjeKursGrad.label,
                                                                            underUtdanning.linjeKursGrad.verdi),
                                                              // TODO Denne! Skal vi ha den fra UII
                                                                Søknadsfelt("Når skal du være elev/student?",
                                                                            Periode(underUtdanning.periode.fra.verdi.month,
                                                                                    underUtdanning.periode.fra.verdi.year,
                                                                                    underUtdanning.periode.til.verdi.month,
                                                                                    underUtdanning.periode.til.verdi.year))
                                                      )),
                              offentligEllerPrivat = Søknadsfelt(underUtdanning.offentligEllerPrivat.label,
                                                                 underUtdanning.offentligEllerPrivat.verdi),
                              hvorMyeSkalDuStudere = Søknadsfelt(underUtdanning.arbeidsmengde.label,
                                                                 underUtdanning.arbeidsmengde.verdi.toInt()),
                              hvaErMåletMedUtdanningen = Søknadsfelt(underUtdanning.målMedUtdanning.label,
                                                                     underUtdanning.målMedUtdanning.verdi),
                              utdanningEtterGrunnskolen = Søknadsfelt(underUtdanning.harTattUtdanningEtterGrunnskolen.label,
                                                                      underUtdanning.harTattUtdanningEtterGrunnskolen.verdi),
                              tidligereUtdanninger = underUtdanning.tidligereUtdanning?.let { mapTidligereUtdanning(it) }
        )
    }

    private fun mapTidligereUtdanning(tidligereUtdanning: List<TidligereUtdanning>): Søknadsfelt<List<Utdanning>> {
        val tidligereUtdanningList = tidligereUtdanning.map {
            Utdanning(Søknadsfelt(it.linjeKursGrad.label,
                                  it.linjeKursGrad.verdi),
                      Søknadsfelt("Når var du elev/student?",
                                  Periode(it.periode.fra.verdi.month,
                                          it.periode.fra.verdi.year,
                                          it.periode.til.verdi.month,
                                          it.periode.til.verdi.year)))
        }
        return Søknadsfelt("Tidligere Utdanning", tidligereUtdanningList)
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
