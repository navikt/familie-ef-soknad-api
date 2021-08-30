package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DokumentFelt
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.kontrakter.ef.ettersending.EttersendingDto
import no.nav.familie.kontrakter.ef.ettersending.EttersendingMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov as DokumentasjonsbehovSøknad

@Component
class EttersendingMapper {

    fun mapTilIntern(dto: EttersendingDto,
                     innsendingMottatt: LocalDateTime): EttersendingMedVedlegg {

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

        val vedleggForSøknadUtenDokumentasjonsbehov = dto.ettersendingForSøknad?.innsending?.map { innsending ->
            innsending.vedlegg.map {
                Vedlegg(
                        id = it.id,
                        navn = it.navn,
                        tittel = innsending.beskrivelse)
            }
        }?.flatten() ?: emptyList()
        val vedleggUtenSøknad = dto.ettersendingUtenSøknad?.innsending?.map { innsending ->
            innsending.vedlegg.map {
                Vedlegg(
                        id = it.id,
                        navn = it.navn,
                        tittel = innsending.beskrivelse
                )
            }
        }?.flatten() ?: emptyList()
        val vedleggMedDokumentasjonsbehovWrapper: Map<String, DokumentasjonWrapper> =
                lagDokumentasjonWrapper(dokumentasjonsbehovTilDokumentService)
        val vedlegg = leggSammenVedleggLister(vedleggMedDokumentasjonsbehovWrapper.values.flatMap { it.vedlegg },
                                              vedleggForSøknadUtenDokumentasjonsbehov,
                                              vedleggUtenSøknad)

        return EttersendingMedVedlegg(
                FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
                vedlegg,
                dto
        )
    }

    private fun leggSammenVedleggLister(vedleggForSøknad: List<Vedlegg>,
                                        vedleggForSøknadÅpen: List<Vedlegg>,
                                        vedleggUtenSøknad: List<Vedlegg>): List<Vedlegg> {
        return vedleggForSøknad + vedleggForSøknadÅpen + vedleggUtenSøknad
    }
}