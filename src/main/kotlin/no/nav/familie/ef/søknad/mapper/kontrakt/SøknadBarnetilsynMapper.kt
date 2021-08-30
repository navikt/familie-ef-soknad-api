package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.Språk
import no.nav.familie.ef.søknad.mapper.kontekst
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.ARBEIDSTID
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.AVTALE_BARNEPASSER
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.FAKTURA_BARNEPASSORDNING
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.ROTERENDE_ARBEIDSTID
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.TRENGER_MER_PASS_ENN_JEVNALDREDE
import no.nav.familie.ef.søknad.mapper.kontrakt.FellesMapper.mapInnsendingsdetaljer
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.tilKontrakt
import no.nav.familie.kontrakter.ef.søknad.BarnetilsynDokumentasjon
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadBarnetilsynMapper() {

    fun mapTilIntern(dto: SøknadBarnetilsynDto,
                     innsendingMottatt: LocalDateTime,
                     skalHenteVedlegg: Boolean = true): SøknadMedVedlegg<SøknadBarnetilsyn> {
        kontekst.set(Språk.fromString(dto.locale))
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

        return SøknadMedVedlegg(barnetilsynSøknad,
                                vedlegg.values.flatMap { it.vedlegg },
                                dto.dokumentasjonsbehov.tilKontrakt(),
                                dto.skalBehandlesINySaksbehandling)
    }

}
