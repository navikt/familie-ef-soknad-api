package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Situasjon
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object SituasjonsMapper {

    fun mapSituasjon(frontendDto: SøknadDto, dokumenter: Map<String, Dokument>): Situasjon {
        val merOmDinSituasjon = frontendDto.merOmDinSituasjon
        return Situasjon(gjelderDetteDeg = mapGjelderDetteDeg(merOmDinSituasjon),
                         sykdom = dokumentfelt("Legeerklæring", dokumenter),
                         barnsSykdom = dokumentfelt("Legeattest for egen sykdom eller sykt barn", dokumenter),
                         manglendeBarnepass = dokumentfelt("Avslag på søknad om barnehageplass, skolefritidsordning e.l.",
                                                           dokumenter),
                         barnMedSærligeBehov = dokumentfelt("Dokumentasjon av særlig tilsynsbehov", dokumenter),
                         utdanningstilbud = dokumentfelt("Dokumentasjon av studieopptak", dokumenter),
                         oppstartNyJobb = merOmDinSituasjon.datoOppstartJobb?.let { Søknadsfelt(it.label, it.verdi) },
                         arbeidskontrakt = dokumentfelt("Dokumentasjon av jobbtilbud", dokumenter),
                         oppstartUtdanning = merOmDinSituasjon.datoOppstartUtdanning?.let { Søknadsfelt(it.label, it.verdi) },
                         sagtOppEllerRedusertStilling = merOmDinSituasjon.sagtOppEllerRedusertStilling?.let {
                             Søknadsfelt(it.label,
                                         it.verdi)
                         },
                         oppsigelseReduksjonÅrsak = merOmDinSituasjon.begrunnelseSagtOppEllerRedusertStilling?.let {
                             Søknadsfelt(it.label,
                                         it.verdi)
                         },
                         oppsigelseReduksjonTidspunkt = merOmDinSituasjon.datoSagtOppEllerRedusertStilling?.let {
                             Søknadsfelt(it.label,
                                         it.verdi)
                         },
                         oppsigelseReduksjonDokumentasjon = dokumentfelt("Dokumentasjon av arbeidsforhold", dokumenter))
    }

    private fun mapGjelderDetteDeg(merOmDinSituasjon: no.nav.familie.ef.søknad.api.dto.søknadsdialog.Situasjon) =
            Søknadsfelt(merOmDinSituasjon.gjelderDetteDeg.label,
                        merOmDinSituasjon.gjelderDetteDeg.verdi)


}


