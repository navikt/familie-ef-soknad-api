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
                         sykdom = dokumentfelt("Legeerklæring", dokumenter),
                         barnsSykdom = dokumentfelt("Legeattest for egen sykdom eller sykt barn", dokumenter),
                         manglendeBarnepass = dokumentfelt("Avslag på søknad om barnehageplass, skolefritidsordning e.l.",
                                                           dokumenter),
                         barnMedSærligeBehov = dokumentfelt("Dokumentasjon av særlig tilsynsbehov", dokumenter),
                         utdanningstilbud = dokumentfelt("Dokumentasjon av studieopptak", dokumenter),
                         oppstartNyJobb = merOmDinSituasjon.datoOppstartJobb?.tilSøknadsfelt(),
                         arbeidskontrakt = dokumentfelt("Dokumentasjon av jobbtilbud", dokumenter),
                         oppstartUtdanning = merOmDinSituasjon.datoOppstartUtdanning?.tilSøknadsfelt(),
                         sagtOppEllerRedusertStilling = merOmDinSituasjon.sagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         oppsigelseReduksjonÅrsak = merOmDinSituasjon.begrunnelseSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         oppsigelseReduksjonTidspunkt = merOmDinSituasjon.datoSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                         oppsigelseReduksjonDokumentasjon = dokumentfelt("Dokumentasjon av arbeidsforhold", dokumenter))
    }

}


