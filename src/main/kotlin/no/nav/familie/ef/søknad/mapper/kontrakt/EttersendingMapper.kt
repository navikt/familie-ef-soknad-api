package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DokumentFelt
import no.nav.familie.ef.søknad.integration.EttersendingRequestData
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.ettersending.EttersendingDto
import no.nav.familie.kontrakter.ef.ettersending.EttersendingMedVedlegg
import no.nav.familie.kontrakter.ef.ettersending.Innsending
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov as DokumentasjonsbehovSøknad

@Component
class EttersendingMapper(private val dokumentService: DokumentService) {

    fun mapTilIntern(dto: EttersendingDto,
                     innsendingMottatt: LocalDateTime,
                     skalHenteVedlegg: Boolean = true): EttersendingRequestData {

        val dokumentasjonsbehovTilDokumentService: List<DokumentasjonsbehovSøknad> =
                dto.ettersendingForSøknad?.dokumentasjonsbehov?.map {
                    DokumentasjonsbehovSøknad(
                            it.label,
                            it.id,
                            it.harSendtInn,
                            it.opplastedeVedlegg.map {
                                DokumentFelt(it.id, it.navn)
                            })
                } ?: emptyList()
        val vedleggForSøknadTilDokumentService: List<DokumentFelt> =
                dto.ettersendingForSøknad?.let { hentDokumentFeltTilDokumentService(it.innsending) } ?: emptyList()
        val vedleggUtenSøknadTilDokumentService: List<DokumentFelt> =
                dto.ettersendingUtenSøknad?.let { hentDokumentFeltTilDokumentService(it.innsending) } ?: emptyList()

        val vedleggData = hentVedlegg(skalHenteVedlegg) {
            leggSammenMapFunksjoner(
                    dokumentService.hentDokumenterFraDokumentFelt(vedleggForSøknadTilDokumentService),
                    dokumentService.hentDokumenter(dokumentasjonsbehovTilDokumentService),
                    dokumentService.hentDokumenterFraDokumentFelt(vedleggUtenSøknadTilDokumentService)
            )
        }

        val vedleggForSøknadUtenDokumentasjonsbehov = dto.ettersendingForSøknad?.innsending?.map {
            Vedlegg(
                    id = it.vedlegg.id,
                    navn = it.vedlegg.navn,
                    tittel = it.beskrivelse
            )
        } ?: emptyList()
        val vedleggUtenSøknad = dto.ettersendingUtenSøknad?.innsending?.map {
            Vedlegg(
                    id = it.vedlegg.id,
                    navn = it.vedlegg.navn,
                    tittel = it.beskrivelse
            )
        } ?: emptyList()
        val vedleggMedDokumentasjonsbehovWrapper: Map<String, DokumentasjonWrapper> =
                lagDokumentasjonWrapper(dokumentasjonsbehovTilDokumentService)
        val vedlegg = leggSammenVedleggLister(vedleggMedDokumentasjonsbehovWrapper.values.flatMap { it.vedlegg },
                                              vedleggForSøknadUtenDokumentasjonsbehov,
                                              vedleggUtenSøknad)

        return EttersendingRequestData(
                EttersendingMedVedlegg(
                        FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
                        vedlegg,
                        dto
                ), vedleggData
        )
    }

    private fun leggSammenVedleggLister(vedleggForSøknad: List<Vedlegg>,
                                        vedleggForSøknadÅpen: List<Vedlegg>,
                                        vedleggUtenSøknad: List<Vedlegg>): List<Vedlegg> {
        return vedleggForSøknad + vedleggForSøknadÅpen + vedleggUtenSøknad
    }

    private fun leggSammenMapFunksjoner(
            vedleggDataForSøknad: Map<String, ByteArray>,
            vedleggDataUtenSøknad: Map<String, ByteArray>,
            vedleggDataForSøknadUtenDokumentasjonsbehov: Map<String, ByteArray>
    ): Map<String, ByteArray> {
        return vedleggDataForSøknad + vedleggDataUtenSøknad + vedleggDataForSøknadUtenDokumentasjonsbehov

    }

    private fun hentDokumentFeltTilDokumentService(innsendingsliste: List<Innsending>): List<DokumentFelt> {
        return innsendingsliste.map {
            DokumentFelt(it.vedlegg.id, it.vedlegg.navn)
        }
    }
}