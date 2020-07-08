package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BarnetilsynDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.integration.BarnetilsynRequestData
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.Barn
import no.nav.familie.kontrakter.ef.søknad.Personalia
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BarnetilsynSøknadMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(barnetilsynDto: BarnetilsynDto,
                     innsendingMottatt: LocalDateTime): BarnetilsynRequestData {


        val vedleggData: Map<String, ByteArray> = hentDokumenter(barnetilsynDto.dokumentasjonsbehov)
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(barnetilsynDto.dokumentasjonsbehov)

        val barnetilsynSøknad = BarnetilsynSøknadKontrakt(barnetilsynDto.person.søker.tilSøknadsFelt(),
                                                          barnetilsynDto.person.barn.tilSøknadsfelt(vedlegg))
        return BarnetilsynRequestData(SøknadBarnetilsyn(barnetilsynSøknad, vedlegg.values.map { it.vedlegg }.flatten()),
                                      vedleggData)
    }

    private fun hentDokumenter(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, ByteArray> {
        return dokumentasjonsbehov.flatMap { dok ->
            dok.opplastedeVedlegg.map {
                it.dokumentId to dokumentServiceService.hentVedlegg(it.dokumentId)
            }
        }.toMap()
    }

}


// TODO flyttes til kontrakter
data class SøknadBarnetilsyn(val søknad: BarnetilsynSøknadKontrakt, val vedlegg: List<Vedlegg>)
data class BarnetilsynSøknadKontrakt(val personalia: Søknadsfelt<Personalia>,
                                     val barn: Søknadsfelt<List<Barn>>)


