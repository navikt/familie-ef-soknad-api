package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Firma
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.*
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.ETABLERER_VIRKSOMHET
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.IKKE_VILLIG_TIL_ARBEID
import no.nav.familie.kontrakter.ef.søknad.*
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Aktivitet as AktivitetDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidsgiver as ArbeidsgiverDto

object AktivitetsMapper : MapperMedVedlegg<AktivitetDto, Aktivitet>("Arbeid, utdanning og andre aktiviteter") {

    override fun mapDto(data: AktivitetDto,
                        vedlegg: Map<String, DokumentasjonWrapper>): Aktivitet {
        return Aktivitet(hvordanErArbeidssituasjonen = data.hvaErDinArbeidssituasjon.tilSøknadsfelt(),
                         arbeidsforhold = data.arbeidsforhold?.let {
                             Søknadsfelt("Om arbeidsforholdet ditt", mapArbeidsforhold(it))
                         },
                         selvstendig = data.firma?.let { Søknadsfelt("Om firmaet du driver", mapOmFirma(it)) },
                         virksomhet = data.etablererEgenVirksomhet?.let { mapEtablererVirksomhet(it, vedlegg) },
                         arbeidssøker = data.arbeidssøker?.let { mapArbeidssøker(it, vedlegg) },
                         underUtdanning = data.underUtdanning?.let { UtdanningMapper.map(it) },
                         aksjeselskap = data.egetAS?.let {
                             Søknadsfelt("Ansatt i eget AS", it.map { aksjeselskap ->
                                 Aksjeselskap(navn = aksjeselskap.navn.tilSøknadsfelt(),
                                              arbeidsmengde = aksjeselskap.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall))
                             })
                         },
                         erIArbeid = data.erIArbeid?.tilSøknadsfelt(),
                         erIArbeidDokumentasjon = dokumentfelt(DokumentIdentifikator.FOR_SYK_TIL_Å_JOBBE, vedlegg)
        )
    }

    private fun mapArbeidssøker(arbeidssøker: no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker,
                                vedlegg: Map<String, DokumentasjonWrapper>): Søknadsfelt<Arbeidssøker> {
        return Søknadsfelt("Når du er arbeidssøker",
                           Arbeidssøker(
                                   registrertSomArbeidssøkerNav = arbeidssøker.registrertSomArbeidssøkerNav.tilSøknadsfelt(),
                                   villigTilÅTaImotTilbudOmArbeid = arbeidssøker.villigTilÅTaImotTilbudOmArbeid.tilSøknadsfelt(),
                                   kanDuBegynneInnenEnUke = arbeidssøker.kanBegynneInnenEnUke.tilSøknadsfelt(),
                                   kanDuSkaffeBarnepassInnenEnUke = arbeidssøker.kanSkaffeBarnepassInnenEnUke?.tilSøknadsfelt(),
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
                           arbeidsmengde = firma.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall),
                           hvordanSerArbeidsukenUt = firma.arbeidsuke.tilSøknadsfelt())
    }

    private fun mapArbeidsforhold(arbeidsforhold: List<ArbeidsgiverDto>): List<Arbeidsgiver> {

        return arbeidsforhold.map { arbeid ->
            Arbeidsgiver(arbeidsgivernavn = arbeid.navn.tilSøknadsfelt(),
                         arbeidsmengde = arbeid.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall),
                         fastEllerMidlertidig = arbeid.ansettelsesforhold.tilSøknadsfelt(),
                         harSluttdato = arbeid.harSluttDato?.tilSøknadsfelt(),
                         sluttdato = arbeid.sluttdato?.tilSøknadsfelt()
            )
        }
    }
}

