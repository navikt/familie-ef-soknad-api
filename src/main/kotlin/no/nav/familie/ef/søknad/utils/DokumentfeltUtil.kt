package no.nav.familie.ef.søknad.utils

import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Dokumentasjon
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Vedlegg

object DokumentfeltUtil {
    fun dokumentfelt(
        dokumentIdentifikator: DokumentIdentifikator,
        vedleggMap: Map<String, DokumentasjonWrapper>,
    ): Søknadsfelt<Dokumentasjon>? {
        val dokumentasjon = vedleggMap[dokumentIdentifikator.name]
        return dokumentasjon?.let {
            val dokumenter = it.vedlegg.map { vedlegg -> Dokument(vedlegg.id, vedlegg.navn) }
            Søknadsfelt(it.label, Dokumentasjon(it.harSendtInnTidligere, dokumenter))
        }
    }
}

data class DokumentasjonWrapper(
    val label: String,
    val harSendtInnTidligere: Søknadsfelt<Boolean>,
    val vedlegg: List<Vedlegg>,
)
