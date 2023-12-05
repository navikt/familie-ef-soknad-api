package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Firma
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.Språktekster.ArbeidUtanningOgAndreAktiviteter
import no.nav.familie.ef.søknad.mapper.Språktekster.NårDuErArbeidssøker
import no.nav.familie.ef.søknad.mapper.Språktekster.OmAksjeselskapetDitt
import no.nav.familie.ef.søknad.mapper.Språktekster.OmArbeidsforholdet
import no.nav.familie.ef.søknad.mapper.Språktekster.OmFirmaDuDriver
import no.nav.familie.ef.søknad.mapper.Språktekster.OmVirksomhetenDuEtablerer
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.ETABLERER_VIRKSOMHET
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.IKKE_VILLIG_TIL_ARBEID
import no.nav.familie.ef.søknad.mapper.tilBooleanFelt
import no.nav.familie.ef.søknad.mapper.tilDatoFelt
import no.nav.familie.ef.søknad.mapper.tilHeltall
import no.nav.familie.ef.søknad.mapper.tilListFelt
import no.nav.familie.ef.søknad.mapper.tilNullableBooleanFelt
import no.nav.familie.ef.søknad.mapper.tilNullableTekstFelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.ef.søknad.mapper.tilTekstFelt
import no.nav.familie.kontrakter.ef.søknad.Aksjeselskap
import no.nav.familie.kontrakter.ef.søknad.Aktivitet
import no.nav.familie.kontrakter.ef.søknad.Arbeidsgiver
import no.nav.familie.kontrakter.ef.søknad.Arbeidssøker
import no.nav.familie.kontrakter.ef.søknad.Selvstendig
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Virksomhet
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Aksjeselskap as AksjeselskapDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Aktivitet as AktivitetDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidsgiver as ArbeidsgiverDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker as ArbeidssøkerDto

object AktivitetsMapper : MapperMedVedlegg<AktivitetDto, Aktivitet>(ArbeidUtanningOgAndreAktiviteter) {

    override fun mapDto(
        data: AktivitetDto,
        vedlegg: Map<String, DokumentasjonWrapper>,
    ): Aktivitet {
        return Aktivitet(
            hvordanErArbeidssituasjonen = data.hvaErDinArbeidssituasjon.tilSøknadsfelt(),
            arbeidsforhold = data.arbeidsforhold?.let {
                Søknadsfelt(OmArbeidsforholdet.hentTekst(), mapArbeidsforhold(it))
            },
            firmaer = data.firmaer?.let { Søknadsfelt(OmFirmaDuDriver.hentTekst(), mapOmFirmaer(it)) },
            virksomhet = data.etablererEgenVirksomhet?.let { mapEtablererVirksomhet(it, vedlegg) },
            arbeidssøker = data.arbeidssøker?.let { mapArbeidssøker(it, vedlegg) },
            underUtdanning = data.underUtdanning?.let { UtdanningMapper.map(it) },
            aksjeselskap = data.egetAS?.let {
                Søknadsfelt(
                    OmAksjeselskapetDitt.hentTekst(),
                    it.map { aksjeselskap ->
                        Aksjeselskap(
                            navn = aksjeselskap.navn.tilSøknadsfelt(),
                            arbeidsmengde = aksjeselskap.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall),
                        )
                    },
                )
            },
            erIArbeid = data.erIArbeid?.tilSøknadsfelt(),
            erIArbeidDokumentasjon = dokumentfelt(DokumentIdentifikator.FOR_SYK_TIL_Å_JOBBE, vedlegg),
        )
    }

    fun mapTilDto(aktivitet: Aktivitet): AktivitetDto {
        return AktivitetDto(
            arbeidsforhold = mapTilArbeidsgiverDto(aktivitet.arbeidsforhold?.verdi ?: emptyList()),
            arbeidssøker = mapTilArbeidssøkerDto(aktivitet.arbeidssøker?.verdi),
            firmaer = mapTilFirmaerDto(aktivitet.firmaer?.verdi),
            hvaErDinArbeidssituasjon = aktivitet.hvordanErArbeidssituasjonen.tilListFelt(),
            underUtdanning = UtdanningMapper.mapTilDto(aktivitet.underUtdanning?.verdi),
            etablererEgenVirksomhet = aktivitet.virksomhet.tilNullableTekstFelt(),
            egetAS = mapTilAksjeselskapDto(aktivitet.aksjeselskap?.verdi),
            erIArbeid = aktivitet.erIArbeid.tilNullableTekstFelt(),
        )
    }

    private fun mapArbeidssøker(
        arbeidssøker: ArbeidssøkerDto,
        vedlegg: Map<String, DokumentasjonWrapper>,
    ): Søknadsfelt<Arbeidssøker> {
        return Søknadsfelt(
            NårDuErArbeidssøker.hentTekst(),
            Arbeidssøker(
                registrertSomArbeidssøkerNav = arbeidssøker.registrertSomArbeidssøkerNav.tilSøknadsfelt(),
                villigTilÅTaImotTilbudOmArbeid = arbeidssøker.villigTilÅTaImotTilbudOmArbeid.tilSøknadsfelt(),
                kanDuBegynneInnenEnUke = arbeidssøker.kanBegynneInnenEnUke.tilSøknadsfelt(),
                kanDuSkaffeBarnepassInnenEnUke = arbeidssøker.kanSkaffeBarnepassInnenEnUke?.tilSøknadsfelt(),
                hvorØnskerDuArbeid = arbeidssøker.hvorØnskerSøkerArbeid.tilSøknadsfelt(),
                ønskerDuMinst50ProsentStilling = arbeidssøker.ønskerSøker50ProsentStilling.tilSøknadsfelt(),
                ikkeVilligTilÅTaImotTilbudOmArbeidDokumentasjon = dokumentfelt(
                    IKKE_VILLIG_TIL_ARBEID,
                    vedlegg,
                ),
            ),
        )
    }

    private fun mapTilArbeidssøkerDto(
        arbeidssøker: Arbeidssøker?,
    ): ArbeidssøkerDto? {
        if (arbeidssøker == null) return null
        return ArbeidssøkerDto(
            hvorØnskerSøkerArbeid = arbeidssøker.hvorØnskerDuArbeid.tilTekstFelt(),
            kanBegynneInnenEnUke = arbeidssøker.kanDuBegynneInnenEnUke.tilBooleanFelt(),
            kanSkaffeBarnepassInnenEnUke = arbeidssøker.kanDuSkaffeBarnepassInnenEnUke.tilNullableBooleanFelt(),
            registrertSomArbeidssøkerNav = arbeidssøker.registrertSomArbeidssøkerNav.tilBooleanFelt(),
            villigTilÅTaImotTilbudOmArbeid = arbeidssøker.villigTilÅTaImotTilbudOmArbeid.tilBooleanFelt(),
            ønskerSøker50ProsentStilling = arbeidssøker.ønskerDuMinst50ProsentStilling.tilBooleanFelt(),
        )
    }

    private fun mapEtablererVirksomhet(
        it: TekstFelt,
        vedlegg: Map<String, DokumentasjonWrapper>,
    ): Søknadsfelt<Virksomhet> {
        return Søknadsfelt(
            OmVirksomhetenDuEtablerer.hentTekst(),
            Virksomhet(
                it.tilSøknadsfelt(),
                dokumentfelt(ETABLERER_VIRKSOMHET, vedlegg),
            ),
        )
    }

    private fun mapOmFirmaer(firmaer: List<Firma>): List<Selvstendig> {
        return firmaer.map { firma -> mapOmFirma(firma) }
    }

    private fun mapOmFirma(firma: Firma): Selvstendig {
        return Selvstendig(
            firmanavn = firma.navn.tilSøknadsfelt(),
            organisasjonsnummer = firma.organisasjonsnummer.tilSøknadsfelt(),
            etableringsdato = firma.etableringsdato.tilSøknadsfelt(),
            arbeidsmengde = firma.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall),
            hvordanSerArbeidsukenUt = firma.arbeidsuke.tilSøknadsfelt(),
        )
    }

    private fun mapTilFirmaerDto(firmaer: List<Selvstendig>?): List<Firma> {
        return firmaer?.map {
            Firma(
                navn = it.firmanavn.tilTekstFelt(),
                organisasjonsnummer = it.organisasjonsnummer.tilTekstFelt(),
                etableringsdato = it.etableringsdato.tilDatoFelt(),
                arbeidsmengde = it.arbeidsmengde.tilNullableTekstFelt(),
                arbeidsuke = it.hvordanSerArbeidsukenUt.tilTekstFelt(),
            )
        } ?: emptyList()
    }

    fun mapArbeidsforhold(arbeidsforhold: List<ArbeidsgiverDto>): List<Arbeidsgiver> {
        return arbeidsforhold.map { arbeid ->
            Arbeidsgiver(
                arbeidsgivernavn = arbeid.navn.tilSøknadsfelt(),
                arbeidsmengde = arbeid.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall),
                fastEllerMidlertidig = arbeid.ansettelsesforhold.tilSøknadsfelt(),
                harSluttdato = arbeid.harSluttDato?.tilSøknadsfelt(),
                sluttdato = arbeid.sluttdato?.tilSøknadsfelt(),
            )
        }
    }

    fun mapTilArbeidsgiverDto(arbeidsforhold: List<Arbeidsgiver>): List<ArbeidsgiverDto> {
        return arbeidsforhold.map { arbeid ->
            ArbeidsgiverDto(
                arbeidsmengde = arbeid.arbeidsmengde?.tilTekstFelt(),
                sluttdato = arbeid.sluttdato?.tilDatoFelt(),
                ansettelsesforhold = TekstFelt(arbeid.fastEllerMidlertidig.label, arbeid.fastEllerMidlertidig.verdi, arbeid.fastEllerMidlertidig.svarId),
                harSluttDato = arbeid.harSluttdato.tilNullableBooleanFelt(),
                id = "dummy",
                navn = TekstFelt(arbeid.arbeidsgivernavn.label, arbeid.arbeidsgivernavn.verdi, arbeid.arbeidsgivernavn.svarId),
            )
        }
    }

    private fun mapTilAksjeselskapDto(aksjeselskap: List<Aksjeselskap>?): List<AksjeselskapDto> {
        return aksjeselskap?.map {
            AksjeselskapDto(
                navn = it.navn.tilTekstFelt(),
                arbeidsmengde = it.arbeidsmengde?.tilTekstFelt(),
            )
        } ?: emptyList()
    }
}
