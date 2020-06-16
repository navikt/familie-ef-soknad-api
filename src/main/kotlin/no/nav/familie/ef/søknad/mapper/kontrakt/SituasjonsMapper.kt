package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Situasjon

object SituasjonsMapper {

    fun mapSituasjon(frontendDto: SøknadDto, dokumenter: Map<String, List<Dokument>>): Situasjon {
        val merOmDinSituasjon = frontendDto.merOmDinSituasjon
        return Situasjon(gjelderDetteDeg = merOmDinSituasjon.gjelderDetteDeg.tilSøknadsfelt(),
                         sykdom = dokumentfelt(SYKDOM, dokumenter),
                         barnsSykdom = dokumentfelt(SYKT_BARN, dokumenter),
                         manglendeBarnepass = dokumentfelt(BARNEPASS, dokumenter),
                         barnMedSærligeBehov = dokumentfelt(BARNETILSYN_BEHOV, dokumenter),
                         utdanningstilbud = dokumentfelt(UTDANNING, dokumenter),
                         oppstartNyJobb = merOmDinSituasjon.datoOppstartJobb?.tilSøknadsfelt(),
                         arbeidskontrakt = dokumentfelt(ARBEIDSKONTRAKT, dokumenter),
                         oppstartUtdanning = merOmDinSituasjon.datoOppstartUtdanning?.tilSøknadsfelt(),
                         sagtOppEllerRedusertStilling = merOmDinSituasjon.sagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         oppsigelseReduksjonÅrsak = merOmDinSituasjon.begrunnelseSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         oppsigelseReduksjonTidspunkt = merOmDinSituasjon.datoSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         reduksjonAvArbeidsforholdDokumentasjon = dokumentfelt(ARBEIDSFORHOLD_REDUSERT_ARBEIDSTID, dokumenter),
                         oppsigelseDokumentasjon = dokumentfelt(ARBEIDSFORHOLD_OPPSIGELSE, dokumenter),
                         lærlingkontrakt = dokumentfelt(LÆRLING, dokumenter))
    }

}


