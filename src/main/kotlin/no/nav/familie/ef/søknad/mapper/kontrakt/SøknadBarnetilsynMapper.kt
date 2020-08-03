package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.integration.SøknadRequestData
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.Innsendingsdetaljer
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadBarnetilsynMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(dto: SøknadBarnetilsynDto,
                     innsendingMottatt: LocalDateTime): SøknadRequestData<SøknadBarnetilsyn> {
        val vedleggData: Map<String, ByteArray> = dokumentServiceService.hentDokumenter(dto.dokumentasjonsbehov)
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)

        val barnetilsynSøknad = SøknadBarnetilsyn(
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
                stønadsstart = Søknadsfelt("Når søker du stønad fra?",
                                           StønadsstartMapper.mapStønadsstart(dto.søknadsdato,
                                                                              dto.søkerFraBestemtMåned)),
                tidligereFakturaer = dokumentfelt(DokumentIdentifikator.TIDLIGERE_FAKTURAER, vedlegg),
                barnepassordningFaktura = dokumentfelt(DokumentIdentifikator.FAKTURA_BARNEPASSORDNING, vedlegg),
                avtaleBarnepasser = dokumentfelt(DokumentIdentifikator.AVTALE_BARNEPASSER, vedlegg)
        )

        return SøknadRequestData(SøknadMedVedlegg(barnetilsynSøknad, vedlegg.values.map { it.vedlegg }.flatten()),
                                 vedleggData)
    }

}
