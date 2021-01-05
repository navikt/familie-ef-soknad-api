package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.integration.SøknadRequestData
import no.nav.familie.ef.søknad.mapper.*
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.*
import no.nav.familie.ef.søknad.mapper.kontrakt.FellesMapper.mapInnsendingsdetaljer
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.BarnetilsynDokumentasjon
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadBarnetilsynMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(dto: SøknadBarnetilsynDto,
                     innsendingMottatt: LocalDateTime): SøknadRequestData<SøknadBarnetilsyn> {
        kontekst.set(Språk.fromString(dto.locale))
        val vedleggData: Map<String, ByteArray> = dokumentServiceService.hentDokumenter(dto.dokumentasjonsbehov)
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)

        val barnetilsynSøknad = SøknadBarnetilsyn(
                innsendingsdetaljer = mapInnsendingsdetaljer(innsendingMottatt),
                personalia = PersonaliaMapper.map(dto.person.søker),
                sivilstandsdetaljer = SivilstandsdetaljerMapper.map(dto.sivilstatus, vedlegg),
                medlemskapsdetaljer = MedlemsskapsMapper.map(dto.medlemskap),
                bosituasjon = BosituasjonMapper.map(dto.bosituasjon, vedlegg),
                sivilstandsplaner = SivilstandsplanerMapper.map(dto.bosituasjon),
                barn = dto.person.barn.tilSøknadsfelt(vedlegg),
                aktivitet = AktivitetsMapper.map(dto.aktivitet, vedlegg),
                stønadsstart = StønadsstartMapper.mapStønadsstart(dto.søknadsdato,
                                                                  dto.søkerFraBestemtMåned),
                dokumentasjon = BarnetilsynDokumentasjon(
                        barnepassordningFaktura = dokumentfelt(FAKTURA_BARNEPASSORDNING, vedlegg),
                        avtaleBarnepasser = dokumentfelt(AVTALE_BARNEPASSER, vedlegg),
                        arbeidstid = dokumentfelt(ARBEIDSTID, vedlegg),
                        roterendeArbeidstid = dokumentfelt(ROTERENDE_ARBEIDSTID, vedlegg),
                        spesielleBehov = dokumentfelt(TRENGER_MER_PASS_ENN_JEVNALDREDE, vedlegg)
                )
        )

        return SøknadRequestData(SøknadMedVedlegg(barnetilsynSøknad,
                                                  vedlegg.values.flatMap { it.vedlegg },
                                                  dto.dokumentasjonsbehov.tilKontrakt()), vedleggData)
    }

}
