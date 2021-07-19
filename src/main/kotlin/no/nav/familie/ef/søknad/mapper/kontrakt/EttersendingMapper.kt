package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DokumentFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.EttersendingDto
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
class EttersendingMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(
        dto: EttersendingDto,
        innsendingMottatt: LocalDateTime
    ): EttersendingRequestData<Ettersending> {

        val dokumentasjonsbehovTilDokumentService: List<Dokumentasjonsbehov> =
            dto.ettersendingForSøknad.dokumentasjonsbehov.map {
                Dokumentasjonsbehov(
                    it.label,
                    it.id,
                    it.harSendtInn,
                    it.opplastedeVedlegg.map { DokumentFelt(it.id, it.navn) })
            }
        val vedleggTilDokumentService: List<DokumentFelt> =
            dto.ettersendingUtenSøknad.innsending.map {
                DokumentFelt(it.vedlegg.id, it.vedlegg.navn)
            }
        val kontekstfriVedleggTilDokumentService: List<DokumentFelt> = dto.ettersendingForSøknad.innsending.map {
            DokumentFelt(it.vedlegg.id, it.vedlegg.navn)
        }

        val vedleggDataForSøknad: Map<String, ByteArray> =
            dokumentServiceService.hentDokumenterFraDokumentFelt(vedleggTilDokumentService)
        val vedleggDataUtenSøknad: Map<String, ByteArray> =
            dokumentServiceService.hentDokumenter(dokumentasjonsbehovTilDokumentService)
        val vedleggDataForSøknadUtenDokumentasjonsbehov: Map<String, ByteArray> =
            dokumentServiceService.hentDokumenterFraDokumentFelt(kontekstfriVedleggTilDokumentService)

        val vedleggData =
            (vedleggDataForSøknad.keys + vedleggDataUtenSøknad.keys + vedleggDataForSøknadUtenDokumentasjonsbehov.keys).associateWith {
                listOf(
                    vedleggDataForSøknad[it],
                    vedleggDataUtenSøknad[it],
                    vedleggDataForSøknadUtenDokumentasjonsbehov[it]
                ).joinToString().toByteArray()
            }
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
}
