package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.integration.SøknadRequestData
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.kontrakt.StønadsstartMapper.mapStønadsstart
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.Innsendingsdetaljer
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.SøknadOvergangsstønad
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.validering.OvergangsstønadValidering
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadOvergangsstønadMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(dto: SøknadOvergangsstønadDto,
                     innsendingMottatt: LocalDateTime): SøknadRequestData<SøknadOvergangsstønad> {
        val vedleggData: Map<String, ByteArray> = dokumentServiceService.hentDokumenter(dto.dokumentasjonsbehov)
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)

        val søknad = SøknadOvergangsstønad(
                innsendingsdetaljer = Søknadsfelt("Innsendingsdetaljer",
                                                  Innsendingsdetaljer(Søknadsfelt("Dato mottatt", innsendingMottatt))),
                personalia = dto.person.søker.tilSøknadsFelt(),
                sivilstandsdetaljer = Søknadsfelt("Årsak til alene med barn",
                                                  SivilstandsdetaljerMapper.mapSivilstandsdetaljer(dto.sivilstatus,
                                                                                                   vedlegg)),
                medlemskapsdetaljer = Søknadsfelt("Opphold i Norge", MedlemsskapsMapper.mapMedlemskap(dto.medlemskap)),
                bosituasjon = Søknadsfelt("Bosituasjonen din",
                                          BosituasjonMapper.mapBosituasjon(dto.bosituasjon, vedlegg)),
                sivilstandsplaner = Søknadsfelt("Fremtidsplaner",
                                                SivilstandsplanerMapper.mapSivilstandsplaner(dto.bosituasjon)),
                barn = dto.person.barn.tilSøknadsfelt(vedlegg),
                aktivitet = Søknadsfelt("Arbeid, utdanning og andre aktiviteter",
                                        AktivitetsMapper.map(dto.aktivitet, vedlegg)),
                situasjon = Søknadsfelt("Mer om situasjonen din", SituasjonsMapper.mapSituasjon(dto, vedlegg)),
                stønadsstart = Søknadsfelt("Når søker du stønad fra?",
                                           mapStønadsstart(dto.merOmDinSituasjon.søknadsdato,
                                                           dto.merOmDinSituasjon.søkerFraBestemtMåned)))

        OvergangsstønadValidering.validate(søknad)

        return SøknadRequestData(SøknadMedVedlegg(søknad, vedlegg.values.map { it.vedlegg }.flatten()), vedleggData)
    }

}
