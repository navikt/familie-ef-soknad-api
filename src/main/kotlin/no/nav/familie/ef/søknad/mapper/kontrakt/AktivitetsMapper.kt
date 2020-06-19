package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Firma
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TidligereUtdanning
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.tilHeltall
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.*
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidsgiver as ArbeidsgiverDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.UnderUtdanning as UnderUtdanningDto

object AktivitetsMapper {
    fun map(frontendDto: SøknadDto,
            vedlegg: Map<String, List<Vedlegg>>): Aktivitet {
        val aktivitet = frontendDto.aktivitet

        return Aktivitet(hvordanErArbeidssituasjonen = aktivitet.hvaErDinArbeidssituasjon.tilSøknadsfelt(),
                         arbeidsforhold = aktivitet.arbeidsforhold?.let {
                             Søknadsfelt("Om arbeidsforholdet ditt", mapArbeidsforhold(it))
                         },
                         selvstendig = aktivitet.firma?.let { Søknadsfelt("Om firmaet du driver", mapOmFirma(it)) },
                         virksomhet = aktivitet.etablererEgenVirksomhet?.let { mapEtablererVirksomhet(it) },
                         arbeidssøker = aktivitet.arbeidssøker?.let { mapArbeidssøker(it, vedlegg) },
                         underUtdanning = aktivitet.underUtdanning?.let { mapUtdanning(it) },
                         aksjeselskap = aktivitet.egetAS?.let {
                             Søknadsfelt("Ansatt i eget AS", it.map { aksjeselskap ->
                                 Aksjeselskap(
                                         navn = aksjeselskap.navn.tilSøknadsfelt(),
                                         arbeidsmengde = aksjeselskap.arbeidsmengde.tilSøknadsfelt(String::tilHeltall)
                                 )
                             }
                             )
                         }
        )
    }

    private fun mapUtdanning(underUtdanning: UnderUtdanningDto): Søknadsfelt<UnderUtdanning> {
        return Søknadsfelt("Utdanningen du skal ta", mapUnderUtdanning(underUtdanning))

    }

    private fun mapUnderUtdanning(underUtdanning: no.nav.familie.ef.søknad.api.dto.søknadsdialog.UnderUtdanning): UnderUtdanning {
        return UnderUtdanning(skoleUtdanningssted = underUtdanning.skoleUtdanningssted.tilSøknadsfelt(),
                              utdanning = Søknadsfelt("Utdanning",
                                                      Utdanning(underUtdanning.linjeKursGrad.tilSøknadsfelt(),
                                                                Søknadsfelt("Når skal du være elev/student?",
                                                                            Periode(underUtdanning.periode.fra.verdi.month,
                                                                                    underUtdanning.periode.fra.verdi.year,
                                                                                    underUtdanning.periode.til.verdi.month,
                                                                                    underUtdanning.periode.til.verdi.year))
                                                      )),
                              offentligEllerPrivat = underUtdanning.offentligEllerPrivat.tilSøknadsfelt(),
                              hvorMyeSkalDuStudere = underUtdanning.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall),
                              heltidEllerDeltid = underUtdanning.heltidEllerDeltid.tilSøknadsfelt(),
                              hvaErMåletMedUtdanningen = underUtdanning.målMedUtdanning?.tilSøknadsfelt(),
                              utdanningEtterGrunnskolen = underUtdanning.harTattUtdanningEtterGrunnskolen.tilSøknadsfelt(),
                              tidligereUtdanninger = underUtdanning.tidligereUtdanning?.let { mapTidligereUtdanning(it) }
        )
    }

    private fun mapTidligereUtdanning(tidligereUtdanning: List<TidligereUtdanning>): Søknadsfelt<List<Utdanning>> {
        val tidligereUtdanningList = tidligereUtdanning.map {
            Utdanning(it.linjeKursGrad.tilSøknadsfelt(),
                      Søknadsfelt("Når var du elev/student?",
                                  Periode(it.periode.fra.verdi.month,
                                          it.periode.fra.verdi.year,
                                          it.periode.til.verdi.month,
                                          it.periode.til.verdi.year)))
        }
        return Søknadsfelt("Tidligere Utdanning", tidligereUtdanningList)
    }

    private fun mapArbeidssøker(arbeidssøker: no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker,
                                vedlegg: Map<String, List<Vedlegg>>): Søknadsfelt<Arbeidssøker> {
        return Søknadsfelt("Når du er arbeidssøker",
                           Arbeidssøker(
                                   registrertSomArbeidssøkerNav = arbeidssøker.registrertSomArbeidssøkerNav.tilSøknadsfelt(),
                                   villigTilÅTaImotTilbudOmArbeid = arbeidssøker.villigTilÅTaImotTilbudOmArbeid.tilSøknadsfelt(),
                                   kanDuBegynneInnenEnUke = arbeidssøker.kanBegynneInnenEnUke.tilSøknadsfelt(),
                                   kanDuSkaffeBarnepassInnenEnUke = arbeidssøker.kanSkaffeBarnepassInnenEnUke.tilSøknadsfelt(),
                                   hvorØnskerDuArbeid = arbeidssøker.hvorØnskerSøkerArbeid.tilSøknadsfelt(),
                                   ønskerDuMinst50ProsentStilling = arbeidssøker.ønskerSøker50ProsentStilling.tilSøknadsfelt(),
                                   ikkeVilligTilÅTaImotTilbudOmArbeidDokumentasjon = dokumentfelt(IKKE_VILLIG_TIL_ARBEID,
                                                                                                  vedlegg))
        )
    }

    private fun mapEtablererVirksomhet(it: TekstFelt): Søknadsfelt<Virksomhet> {
        return Søknadsfelt("Om virksomheten du etablerer", Virksomhet(it.tilSøknadsfelt()))
    }

    private fun mapOmFirma(firma: Firma): Selvstendig {
        return Selvstendig(firmanavn = firma.navn.tilSøknadsfelt(),
                           organisasjonsnummer = firma.organisasjonsnummer.tilSøknadsfelt(),
                           etableringsdato = firma.etableringsdato.tilSøknadsfelt(),
                           arbeidsmengde = firma.arbeidsmengde.tilSøknadsfelt(String::tilHeltall),
                           hvordanSerArbeidsukenUt = firma.arbeidsuke.tilSøknadsfelt())
    }

    private fun mapArbeidsforhold(arbeidsforhold: List<ArbeidsgiverDto>): List<Arbeidsgiver> {

        return arbeidsforhold.map { arbeid ->
            Arbeidsgiver(arbeidsgivernavn = arbeid.navn.tilSøknadsfelt(),
                         arbeidsmengde = arbeid.arbeidsmengde.tilSøknadsfelt(String::tilHeltall  ),
                         fastEllerMidlertidig = arbeid.ansettelsesforhold.tilSøknadsfelt(),
                         harSluttdato = arbeid.harSluttDato?.tilSøknadsfelt(),
                         sluttdato = arbeid.sluttdato?.tilSøknadsfelt()
            )
        }
    }
}

