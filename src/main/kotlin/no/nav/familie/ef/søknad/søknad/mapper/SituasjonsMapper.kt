package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.ARBEIDSFORHOLD_OPPSIGELSE
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.ARBEIDSFORHOLD_REDUSERT_ARBEIDSTID
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.ARBEIDSKONTRAKT
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.BARNEPASS
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.BARNETILSYN_BEHOV
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.LÆRLING
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.SYKDOM
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.SYKT_BARN
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.UTDANNING
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.utils.Språktekster.MerOmSituasjonenDin
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
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
            lærlingkontrakt = dokumentfelt(LÆRLING, vedlegg),
        )
    }
}
