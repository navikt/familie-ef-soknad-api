package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Firma
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TidligereUtdanning
import no.nav.familie.ef.søknad.mapper.*
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.ETABLERER_VIRKSOMHET
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.IKKE_VILLIG_TIL_ARBEID
import no.nav.familie.kontrakter.ef.søknad.*
import org.slf4j.LoggerFactory
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidsgiver as ArbeidsgiverDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.UnderUtdanning as UnderUtdanningDto

object AktivitetsMapper {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun map(frontendDto: SøknadDto, vedlegg: Map<String, DokumentasjonWrapper>): Aktivitet {
        val aktivitet = frontendDto.aktivitet

        try {
            return Aktivitet(hvordanErArbeidssituasjonen = aktivitet.hvaErDinArbeidssituasjon.tilSøknadsfelt(),
                             arbeidsforhold = aktivitet.arbeidsforhold?.let {
                                 Søknadsfelt("Om arbeidsforholdet ditt", mapArbeidsforhold(it))
                             },
                             selvstendig = aktivitet.firma?.let { Søknadsfelt("Om firmaet du driver", mapOmFirma(it)) },
                             virksomhet = aktivitet.etablererEgenVirksomhet?.let { mapEtablererVirksomhet(it, vedlegg) },
                             arbeidssøker = aktivitet.arbeidssøker?.let { mapArbeidssøker(it, vedlegg) },
                             underUtdanning = aktivitet.underUtdanning?.let { mapUtdanning(it) },
                             aksjeselskap = aktivitet.egetAS?.let {
                                 Søknadsfelt("Ansatt i eget AS", it.map { aksjeselskap ->
                                     Aksjeselskap(navn = aksjeselskap.navn.tilSøknadsfelt(),
                                                  arbeidsmengde = aksjeselskap.arbeidsmengde.tilSøknadsfelt(String::tilHeltall))
                                 })
                             })
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av aktivitet.", aktivitet, e)
            throw e
        }
    }

    private fun mapUtdanning(underUtdanning: UnderUtdanningDto): Søknadsfelt<UnderUtdanning> {
        return Søknadsfelt("Utdanningen du skal ta", mapUnderUtdanning(underUtdanning))

    }

    private fun mapUnderUtdanning(underUtdanning: no.nav.familie.ef.søknad.api.dto.søknadsdialog.UnderUtdanning): UnderUtdanning {
        return UnderUtdanning(skoleUtdanningssted = underUtdanning.skoleUtdanningssted.tilSøknadsfelt(),
                              utdanning =
                              Søknadsfelt("Utdanning",
                                          Utdanning(underUtdanning.linjeKursGrad.tilSøknadsfelt(),
                                                    Søknadsfelt("Når skal du være elev/student?",
                                                                Periode(underUtdanning.periode.fra.tilLocalDate().month,
                                                                        underUtdanning.periode.fra.tilLocalDate().year,
                                                                        underUtdanning.periode.til.tilLocalDate().month,
                                                                        underUtdanning.periode.til.tilLocalDate().year)))),
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
                                  Periode(it.periode.fra.tilLocalDate().month,
                                          it.periode.fra.tilLocalDate().year,
                                          it.periode.til.tilLocalDate().month,
                                          it.periode.til.tilLocalDate().year)))
        }
        return Søknadsfelt("Tidligere Utdanning", tidligereUtdanningList)
    }

    private fun mapArbeidssøker(arbeidssøker: no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker,
                                vedlegg: Map<String, DokumentasjonWrapper>): Søknadsfelt<Arbeidssøker> {
        return Søknadsfelt("Når du er arbeidssøker",
                           Arbeidssøker(
                                   registrertSomArbeidssøkerNav = arbeidssøker.registrertSomArbeidssøkerNav.tilSøknadsfelt(),
                                   villigTilÅTaImotTilbudOmArbeid = arbeidssøker.villigTilÅTaImotTilbudOmArbeid.tilSøknadsfelt(),
                                   kanDuBegynneInnenEnUke = arbeidssøker.kanBegynneInnenEnUke.tilSøknadsfelt(),
                                   kanDuSkaffeBarnepassInnenEnUke = arbeidssøker.kanSkaffeBarnepassInnenEnUke.tilSøknadsfelt(),
                                   hvorØnskerDuArbeid = arbeidssøker.hvorØnskerSøkerArbeid.tilSøknadsfelt(),
                                   ønskerDuMinst50ProsentStilling = arbeidssøker.ønskerSøker50ProsentStilling.tilSøknadsfelt(),
                                   ikkeVilligTilÅTaImotTilbudOmArbeidDokumentasjon = dokumentfelt(IKKE_VILLIG_TIL_ARBEID,
                                                                                                  vedlegg)))
    }

    private fun mapEtablererVirksomhet(it: TekstFelt,
                                       vedlegg: Map<String, DokumentasjonWrapper>): Søknadsfelt<Virksomhet> {
        return Søknadsfelt("Om virksomheten du etablerer", Virksomhet(it.tilSøknadsfelt(),
                                                                      dokumentfelt(ETABLERER_VIRKSOMHET, vedlegg)))
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
                         arbeidsmengde = arbeid.arbeidsmengde.tilSøknadsfelt(String::tilHeltall),
                         fastEllerMidlertidig = arbeid.ansettelsesforhold.tilSøknadsfelt(),
                         harSluttdato = arbeid.harSluttDato?.tilSøknadsfelt(),
                         sluttdato = arbeid.sluttdato?.tilSøknadsfelt()
            )
        }
    }
}

