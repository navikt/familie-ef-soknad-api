package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DokumentFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov as DokumentasjonsbehovSøknad
import no.nav.familie.ef.søknad.api.dto.ettersending.EttersendingDto
import no.nav.familie.ef.søknad.api.dto.ettersending.Innsending
import no.nav.familie.ef.søknad.integration.EttersendingRequestData
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.tilKontrakt
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.ettersending.Ettersending
import no.nav.familie.kontrakter.ef.ettersending.EttersendingMedVedlegg
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class EttersendingMapper(private val dokumentService: DokumentService) {

    fun mapTilIntern(
        dto: EttersendingDto,
        innsendingMottatt: LocalDateTime
    ): EttersendingRequestData<Ettersending> {

        val dokumentasjonsbehovTilDokumentService: List<DokumentasjonsbehovSøknad> =
                dto.ettersendingForSøknad?.dokumentasjonsbehov?.map {
                    DokumentasjonsbehovSøknad(
                            it.label,
                            it.id,
                            it.harSendtInn,
                            it.opplastedeVedlegg.map {
                                DokumentFelt(it.id, it.navn) })
                } ?: emptyList()
        val vedleggForSøknadTilDokumentService: List<DokumentFelt> =
                dto.ettersendingForSøknad?.let { hentDokumentFeltTilDokumentService(it.innsending) } ?: emptyList()
        val vedleggUtenSøknadTilDokumentService: List<DokumentFelt> =
                dto.ettersendingUtenSøknad?.let { hentDokumentFeltTilDokumentService(it.innsending) } ?: emptyList()


        val vedleggData = leggSammenMapFunksjoner(
                dokumentService.hentDokumenterFraDokumentFelt(vedleggForSøknadTilDokumentService),
                dokumentService.hentDokumenter(dokumentasjonsbehovTilDokumentService),
                dokumentService.hentDokumenterFraDokumentFelt(vedleggUtenSøknadTilDokumentService)
        )
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dokumentasjonsbehovTilDokumentService)

        val ettersending = Ettersending(
            innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
            fnr = dto.fnr
        )

        return EttersendingRequestData(
            EttersendingMedVedlegg(
                ettersending,
                vedlegg.values.flatMap { it.vedlegg },
                dokumentasjonsbehovTilDokumentService.tilKontrakt()
            ), vedleggData
        )
    }

    private fun leggSammenMapFunksjoner(
        vedleggDataForSøknad: Map<String, ByteArray>,
        vedleggDataUtenSøknad: Map<String, ByteArray>,
        vedleggDataForSøknadUtenDokumentasjonsbehov: Map<String, ByteArray>
    ): Map<String, ByteArray> {
        return (vedleggDataForSøknad.keys + vedleggDataUtenSøknad.keys + vedleggDataForSøknadUtenDokumentasjonsbehov.keys).associateWith {
            listOf(
                vedleggDataForSøknad[it],
                vedleggDataUtenSøknad[it],
                vedleggDataForSøknadUtenDokumentasjonsbehov[it]
            ).joinToString().toByteArray()
        }
    }

    private fun hentDokumentFeltTilDokumentService(innsendingsliste: List<Innsending>): List<DokumentFelt> {
        return innsendingsliste.map {
            DokumentFelt(it.vedlegg.id, it.vedlegg.navn)
        }
    }
}
