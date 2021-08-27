package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadSkolepengerDto
import no.nav.familie.ef.søknad.integration.SøknadRequestData
import no.nav.familie.ef.søknad.mapper.*
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.SkolepengerDokumentasjon
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.SøknadSkolepenger
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadSkolepengerMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(dto: SøknadSkolepengerDto,
                     innsendingMottatt: LocalDateTime,
                     skalHenteVedlegg: Boolean = true): SøknadRequestData<SøknadSkolepenger> {
        kontekst.set(Språk.fromString(dto.locale))
        val vedleggData: Map<String, ByteArray> =
                hentVedlegg(skalHenteVedlegg) { dokumentServiceService.hentDokumenter(dto.dokumentasjonsbehov) }
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)
        val søknadSkolepenger = SøknadSkolepenger(
                innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
                personalia = PersonaliaMapper.map(dto.person.søker),
                barn = dto.person.barn.tilSøknadsfelt(vedlegg),
                sivilstandsdetaljer = SivilstandsdetaljerMapper.map(dto.sivilstatus, vedlegg),
                medlemskapsdetaljer = MedlemsskapsMapper.map(dto.medlemskap),
                bosituasjon = BosituasjonMapper.map(dto.bosituasjon, vedlegg),
                sivilstandsplaner = SivilstandsplanerMapper.map(dto.bosituasjon),
                utdanning = UtdanningMapper.map(dto.utdanning),
                dokumentasjon = SkolepengerDokumentasjon(
                        utdanningsutgifter = dokumentfelt(DokumentIdentifikator.UTGIFTER_UTDANNING, vedlegg)
                )
        )

        return SøknadRequestData(SøknadMedVedlegg(søknadSkolepenger,
                                                  vedlegg.values.flatMap { it.vedlegg },
                                                  dto.dokumentasjonsbehov.tilKontrakt(),
                                                  dto.skalBehandlesINySaksbehandling), vedleggData)
    }

}
