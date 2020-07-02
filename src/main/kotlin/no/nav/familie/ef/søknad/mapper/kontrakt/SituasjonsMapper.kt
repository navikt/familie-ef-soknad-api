package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.*
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Situasjon

object SituasjonsMapper {

    fun mapSituasjon(frontendDto: SøknadDto, vedlegg: Map<String, DokumentasjonWrapper>): Situasjon {
        val merOmDinSituasjon = frontendDto.merOmDinSituasjon
        return Situasjon(gjelderDetteDeg = merOmDinSituasjon.gjelderDetteDeg.tilSøknadsfelt(),
                         sykdom = dokumentfelt(SYKDOM, vedlegg),
                         barnsSykdom = dokumentfelt(SYKT_BARN, vedlegg),
                         manglendeBarnepass = dokumentfelt(BARNEPASS, vedlegg),
                         barnMedSærligeBehov = dokumentfelt(BARNETILSYN_BEHOV, vedlegg),
                         utdanningstilbud = dokumentfelt(UTDANNING, vedlegg),
                         oppstartNyJobb = frontendDto.aktivitet.datoOppstartJobb?.tilSøknadsfelt(),
                         arbeidskontrakt = dokumentfelt(ARBEIDSKONTRAKT, vedlegg),
                         sagtOppEllerRedusertStilling = merOmDinSituasjon.sagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         oppsigelseReduksjonÅrsak = merOmDinSituasjon.begrunnelseSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         oppsigelseReduksjonTidspunkt = merOmDinSituasjon.datoSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         reduksjonAvArbeidsforholdDokumentasjon = dokumentfelt(ARBEIDSFORHOLD_REDUSERT_ARBEIDSTID, vedlegg),
                         oppsigelseDokumentasjon = dokumentfelt(ARBEIDSFORHOLD_OPPSIGELSE, vedlegg),
                         lærlingkontrakt = dokumentfelt(LÆRLING, vedlegg))
    }

}


