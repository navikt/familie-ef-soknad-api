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

    fun mapTilIntern(dto: EttersendingDto,
                     innsendingMottatt: LocalDateTime): EttersendingRequestData<Ettersending>{

        val dokumentasjonsbehovTilDokumentService: List<Dokumentasjonsbehov> = dto.dokumentasjonsbehov.map {Dokumentasjonsbehov(it.label, it.id, it.harSendtInn, it.opplastedeVedlegg.map { DokumentFelt(it.id, it.navn) })}

        val vedleggData: Map<String, ByteArray> = dokumentServiceService.hentDokumenter(dokumentasjonsbehovTilDokumentService)
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dokumentasjonsbehovTilDokumentService)

        val ettersending = Ettersending(
                innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
                personalia = PersonaliaMapper.map(dto.person.søker),)


        return EttersendingRequestData(EttersendingMedVedlegg(ettersending,
                                                              vedlegg.values.flatMap { it.vedlegg },
                                                              dokumentasjonsbehovTilDokumentService.tilKontrakt()), vedleggData)
    }

}
