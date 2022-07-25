package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.Språktekster.MerOmSituasjonenDin
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.ARBEIDSFORHOLD_OPPSIGELSE
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.ARBEIDSFORHOLD_REDUSERT_ARBEIDSTID
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.ARBEIDSKONTRAKT
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.BARNEPASS
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.BARNETILSYN_BEHOV
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.LÆRLING
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.SYKDOM
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.SYKT_BARN
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.UTDANNING
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Situasjon

object SituasjonsMapper : MapperMedVedlegg<SøknadOvergangsstønadDto, Situasjon>(MerOmSituasjonenDin) {

    override fun mapDto(data: SøknadOvergangsstønadDto, vedlegg: Map<String, DokumentasjonWrapper>): Situasjon {
        val merOmDinSituasjon = data.merOmDinSituasjon
        return Situasjon(
            gjelderDetteDeg = merOmDinSituasjon.gjelderDetteDeg.tilSøknadsfelt(),
            sykdom = dokumentfelt(SYKDOM, vedlegg),
            barnsSykdom = dokumentfelt(SYKT_BARN, vedlegg),
            manglendeBarnepass = dokumentfelt(BARNEPASS, vedlegg),
            barnMedSærligeBehov = dokumentfelt(BARNETILSYN_BEHOV, vedlegg),
            utdanningstilbud = dokumentfelt(UTDANNING, vedlegg),
            oppstartNyJobb = data.aktivitet.datoOppstartJobb?.tilSøknadsfelt(),
            arbeidskontrakt = dokumentfelt(ARBEIDSKONTRAKT, vedlegg),
            sagtOppEllerRedusertStilling = merOmDinSituasjon.sagtOppEllerRedusertStilling?.tilSøknadsfelt(),
            oppsigelseReduksjonÅrsak =
            merOmDinSituasjon.begrunnelseSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
            oppsigelseReduksjonTidspunkt = merOmDinSituasjon.datoSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
            reduksjonAvArbeidsforholdDokumentasjon = dokumentfelt(ARBEIDSFORHOLD_REDUSERT_ARBEIDSTID, vedlegg),
            oppsigelseDokumentasjon = dokumentfelt(ARBEIDSFORHOLD_OPPSIGELSE, vedlegg),
            lærlingkontrakt = dokumentfelt(LÆRLING, vedlegg)
        )
    }
}
