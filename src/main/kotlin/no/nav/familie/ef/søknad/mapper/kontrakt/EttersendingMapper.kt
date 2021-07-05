package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.EttersendingDto
import no.nav.familie.ef.søknad.integration.SøknadRequestData
import no.nav.familie.ef.søknad.mapper.*
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.SøknadEttersending
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class EttersendingMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(dto: EttersendingDto,
                     innsendingMottatt: LocalDateTime): SøknadRequestData<SøknadEttersending>{
        val vedleggData: Map<String, ByteArray> = dokumentServiceService.hentDokumenter(dto.dokumentasjonsbehov)
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)

        val søknad = SøknadEttersending(
                innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
                personalia = PersonaliaMapper.map(dto.person.søker),)


        return SøknadRequestData(SøknadMedVedlegg(søknad,
                                                  vedlegg.values.flatMap { it.vedlegg },
                                                 dto.dokumentasjonsbehov.tilKontrakt()), vedleggData)
    }

}
