package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DokumentFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.EttersendingDto
import no.nav.familie.ef.søknad.integration.EttersendingRequestData
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.tilKontrakt
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.Ettersending
import no.nav.familie.kontrakter.ef.søknad.EttersendingMedVedlegg
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class EttersendingMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(dto: EttersendingDto,
                     innsendingMottatt: LocalDateTime): EttersendingRequestData<Ettersending> {

        val vedleggData: Map<String, ByteArray>
        val ettersending: Ettersending

        if (dto.søknadMedVedlegg === null) {
            val vedleggTilDokumentService =
                dto.åpenInnsendingMedStønadType!!.åpenInnsending.map {
                    DokumentFelt(it.vedlegg.id, it.vedlegg.navn)
                }
            vedleggData = dokumentServiceService.hentDokumenterFraDokumentFelt(vedleggTilDokumentService)
            ettersending = Ettersending(
                innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
                fnr = dto.fnr
            )
            return EttersendingRequestData(EttersendingMedVedlegg(ettersending, null), vedleggData)
        }

        else if (dto.åpenInnsendingMedStønadType === null) {
            val dokumentasjonsbehovTilDokumentService: List<Dokumentasjonsbehov> =
                dto.søknadMedVedlegg.dokumentasjonsbehov.map {
                    Dokumentasjonsbehov(
                        it.label,
                        it.id,
                        it.harSendtInn,
                        it.opplastedeVedlegg.map { DokumentFelt(it.id, it.navn) })
                }
            vedleggData = dokumentServiceService.hentDokumenter(dokumentasjonsbehovTilDokumentService)
            val vedlegg: Map<String, DokumentasjonWrapper> =
                lagDokumentasjonWrapper(dokumentasjonsbehovTilDokumentService)
            ettersending = Ettersending(
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

        val dokumentasjonsbehovTilDokumentService: List<Dokumentasjonsbehov> =
            dto.søknadMedVedlegg.dokumentasjonsbehov.map {
                Dokumentasjonsbehov(
                    it.label,
                    it.id,
                    it.harSendtInn,
                    it.opplastedeVedlegg.map { DokumentFelt(it.id, it.navn) })
            }

        val vedleggTilDokumentService =
            dto.åpenInnsendingMedStønadType.åpenInnsending.map {
                DokumentFelt(it.vedlegg.id, it.vedlegg.navn)
            }

        val vedleggDataSøknad: Map<String, ByteArray> =
            dokumentServiceService.hentDokumenterFraDokumentFelt(vedleggTilDokumentService)
        val vedleggDataÅpen: Map<String, ByteArray> =
            dokumentServiceService.hentDokumenter(dokumentasjonsbehovTilDokumentService)
        vedleggData = (vedleggDataÅpen.keys + vedleggDataSøknad.keys).associateWith{ listOf(vedleggDataÅpen[it], vedleggDataSøknad[it]).joinToString().toByteArray() }

        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dokumentasjonsbehovTilDokumentService)

        ettersending = Ettersending(
            innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
            fnr = dto.fnr)

        return EttersendingRequestData(EttersendingMedVedlegg(ettersending,
        vedlegg.values.flatMap { it.vedlegg },
        dokumentasjonsbehovTilDokumentService.tilKontrakt()), vedleggData)
    }
}
