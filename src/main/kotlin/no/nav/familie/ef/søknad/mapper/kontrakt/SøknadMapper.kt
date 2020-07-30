package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Situasjon
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.integration.SøknadRequestData
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.tilLocalDateEllerNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.*
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(frontendDto: SøknadDto,
                     innsendingMottatt: LocalDateTime): SøknadRequestData {
        val vedleggData: Map<String, ByteArray> = dokumentServiceService.hentDokumenter(frontendDto.dokumentasjonsbehov)
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(frontendDto.dokumentasjonsbehov)

        val søknad = Søknad(
                innsendingsdetaljer = Søknadsfelt("Innsendingsdetaljer",
                                                  Innsendingsdetaljer(Søknadsfelt("Dato mottatt", innsendingMottatt))),
                personalia = frontendDto.person.søker.tilSøknadsFelt(),
                sivilstandsdetaljer = Søknadsfelt("Årsak til alene med barn",
                                                  SivilstandsdetaljerMapper.mapSivilstandsdetaljer(frontendDto, vedlegg)),
                medlemskapsdetaljer = Søknadsfelt("Opphold i Norge", MedlemsskapsMapper.mapMedlemskap(frontendDto)),
                bosituasjon = Søknadsfelt("Bosituasjonen din",
                                          BosituasjonMapper.mapBosituasjon(frontendDto.bosituasjon, vedlegg)),
                sivilstandsplaner = Søknadsfelt("Fremtidsplaner",
                                                SivilstandsplanerMapper.mapSivilstandsplaner(frontendDto.bosituasjon)),
                barn = frontendDto.person.barn.tilSøknadsfelt(vedlegg),
                aktivitet = Søknadsfelt("Arbeid, utdanning og andre aktiviteter", AktivitetsMapper.map(frontendDto, vedlegg)),
                situasjon = Søknadsfelt("Mer om situasjonen din", SituasjonsMapper.mapSituasjon(frontendDto, vedlegg)),
                stønadsstart = Søknadsfelt("Når søker du stønad fra?", stønadsstart(frontendDto.merOmDinSituasjon)))

        return SøknadRequestData(SøknadMedVedlegg(søknad, vedlegg.values.map { it.vedlegg }.flatten()), vedleggData)
    }

    private fun stønadsstart(merOmDinSituasjon: Situasjon): Stønadsstart {
        val month = merOmDinSituasjon.søknadsdato?.tilLocalDateEllerNull()?.month
        val year = merOmDinSituasjon.søknadsdato?.tilLocalDateEllerNull()?.year
        return Stønadsstart(month?.let { Søknadsfelt("Fra måned", month) },
                            year?.let { Søknadsfelt("Fra år", year) },
                            merOmDinSituasjon.søkerFraBestemtMåned.tilSøknadsfelt())
    }


}
