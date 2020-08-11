package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadSkolepengerDto
import no.nav.familie.ef.søknad.integration.SøknadRequestData
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.SkolepengerDokumentasjon
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.SøknadSkolepenger
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadSkolepengerMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(dto: SøknadSkolepengerDto,
                     innsendingMottatt: LocalDateTime): SøknadRequestData<SøknadSkolepenger> {
        val vedleggData: Map<String, ByteArray> = dokumentServiceService.hentDokumenter(dto.dokumentasjonsbehov)
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)

        val barnetilsynSøknad = SøknadSkolepenger(
                innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
                personalia = PersonaliaMapper.map(dto.person.søker),
                sivilstandsdetaljer = SivilstandsdetaljerMapper.map(dto.sivilstatus, vedlegg),
                medlemskapsdetaljer = MedlemsskapsMapper.map(dto.medlemskap),
                bosituasjon = BosituasjonMapper.map(dto.bosituasjon, vedlegg),
                sivilstandsplaner = SivilstandsplanerMapper.map(dto.bosituasjon),
                utdanning = UtdanningMapper.map(dto.utdanning),
                dokumentasjon = SkolepengerDokumentasjon(
                        semesteravgift = dokumentfelt(DokumentIdentifikator.SEMESTERAVGIFT, vedlegg),
                        studieavgift = dokumentfelt(DokumentIdentifikator.STUDIEAVGIFT, vedlegg),
                        eksamensgebyr = dokumentfelt(DokumentIdentifikator.EKSAMENSGEBYR, vedlegg)
                )
        )

        return SøknadRequestData(SøknadMedVedlegg(barnetilsynSøknad, vedlegg.values.map { it.vedlegg }.flatten()),
                                 vedleggData)
    }

}
