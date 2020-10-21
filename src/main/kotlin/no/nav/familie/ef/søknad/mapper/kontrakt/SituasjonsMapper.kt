package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.*
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Situasjon
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object SituasjonsMapper : MapperMedVedlegg<SøknadOvergangsstønadDto, Situasjon>("Mer om situasjonen din") {

    override fun mapDto(frontendDto: SøknadOvergangsstønadDto, vedlegg: Map<String, DokumentasjonWrapper>): Situasjon {
        val merOmDinSituasjon = frontendDto.merOmDinSituasjon
        val gjelderDetteDegAlternativer = merOmDinSituasjon.gjelderDetteDeg.alternativer ?: listOf(
                "Jeg er syk",
                "Barnet mitt er sykt",
                "Jeg har søkt om barnepass, men ikke fått plass enda",
                "Jeg har barn som trenger særlig tilsyn på grunn av fysiske, psykiske eller store sosiale problemer",
                "Nei") // TODO Fjern elvis når vi får data fra UI - gjør mapping av alternativer i tilSøknadsfelt
        return Situasjon(gjelderDetteDeg = Søknadsfelt(merOmDinSituasjon.gjelderDetteDeg.label,
                                                       merOmDinSituasjon.gjelderDetteDeg.verdi,
                                                       gjelderDetteDegAlternativer),
                         sykdom = dokumentfelt(SYKDOM, vedlegg),
                         barnsSykdom = dokumentfelt(SYKT_BARN, vedlegg),
                         manglendeBarnepass = dokumentfelt(BARNEPASS, vedlegg),
                         barnMedSærligeBehov = dokumentfelt(BARNETILSYN_BEHOV, vedlegg),
                         utdanningstilbud = dokumentfelt(UTDANNING, vedlegg),
                         oppstartNyJobb = frontendDto.aktivitet.datoOppstartJobb?.tilSøknadsfelt(),
                         arbeidskontrakt = dokumentfelt(ARBEIDSKONTRAKT, vedlegg),
                         sagtOppEllerRedusertStilling = merOmDinSituasjon.sagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         oppsigelseReduksjonÅrsak =
                         merOmDinSituasjon.begrunnelseSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         oppsigelseReduksjonTidspunkt = merOmDinSituasjon.datoSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         reduksjonAvArbeidsforholdDokumentasjon = dokumentfelt(ARBEIDSFORHOLD_REDUSERT_ARBEIDSTID, vedlegg),
                         oppsigelseDokumentasjon = dokumentfelt(ARBEIDSFORHOLD_OPPSIGELSE, vedlegg),
                         lærlingkontrakt = dokumentfelt(LÆRLING, vedlegg))
    }

}
