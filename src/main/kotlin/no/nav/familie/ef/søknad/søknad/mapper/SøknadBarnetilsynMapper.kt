package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.ARBEIDSTID
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.AVTALE_BARNEPASSER
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.FAKTURA_BARNEPASSORDNING
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.ROTERENDE_ARBEIDSTID
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.TRENGER_MER_PASS_ENN_JEVNALDREDE
import no.nav.familie.ef.søknad.søknad.domain.PersonTilGjenbruk
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynGjenbrukDto
import no.nav.familie.ef.søknad.søknad.mapper.FellesMapper.mapInnsendingsdetaljer
import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.utils.Språk
import no.nav.familie.ef.søknad.utils.kontekst
import no.nav.familie.ef.søknad.utils.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.tilKontrakt
import no.nav.familie.kontrakter.ef.søknad.BarnetilsynDokumentasjon
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadBarnetilsynMapper {
    fun mapTilIntern(
        dto: SøknadBarnetilsynDto,
        innsendingMottatt: LocalDateTime,
        skalHenteVedlegg: Boolean = true,
    ): SøknadMedVedlegg<SøknadBarnetilsyn> {
        kontekst.set(Språk.fromString(dto.locale))
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)

        val barnetilsynSøknad =
            SøknadBarnetilsyn(
                innsendingsdetaljer = mapInnsendingsdetaljer(innsendingMottatt, dto.datoPåbegyntSøknad),
                personalia = PersonaliaMapper.map(dto.person.søker),
                adresseopplysninger =
                    AdresseopplysningerMapper.map(
                        AdresseopplysningerData(
                            dto.søkerBorPåRegistrertAdresse,
                            dto.adresseopplysninger,
                        ),
                        vedlegg,
                    ),
                sivilstandsdetaljer = SivilstandsdetaljerMapper.map(dto.sivilstatus, vedlegg),
                medlemskapsdetaljer = MedlemsskapsMapper.map(dto.medlemskap),
                bosituasjon = BosituasjonMapper.map(dto.bosituasjon, vedlegg),
                sivilstandsplaner = SivilstandsplanerMapper.map(dto.bosituasjon),
                barn =
                    dto.person.barn
                        .filter { it.skalHaBarnepass?.verdi == true }
                        .tilSøknadsfelt(vedlegg),
                aktivitet = AktivitetsMapper.map(dto.aktivitet, vedlegg),
                stønadsstart =
                    StønadsstartMapper.mapStønadsstart(
                        dto.søknadsdato,
                        dto.søkerFraBestemtMåned,
                    ),
                dokumentasjon =
                    BarnetilsynDokumentasjon(
                        barnepassordningFaktura = dokumentfelt(FAKTURA_BARNEPASSORDNING, vedlegg),
                        avtaleBarnepasser = dokumentfelt(AVTALE_BARNEPASSER, vedlegg),
                        arbeidstid = dokumentfelt(ARBEIDSTID, vedlegg),
                        roterendeArbeidstid = dokumentfelt(ROTERENDE_ARBEIDSTID, vedlegg),
                        spesielleBehov = dokumentfelt(TRENGER_MER_PASS_ENN_JEVNALDREDE, vedlegg),
                    ),
            )

        return SøknadMedVedlegg(
            barnetilsynSøknad,
            vedlegg.values.flatMap { it.vedlegg },
            dto.dokumentasjonsbehov.tilKontrakt(),
            dto.skalBehandlesINySaksbehandling,
        )
    }

    fun mapTilDto(søknadBarnetilsyn: SøknadBarnetilsyn?): SøknadBarnetilsynGjenbrukDto? {
        if (søknadBarnetilsyn == null) return null
        return SøknadBarnetilsynGjenbrukDto(
            sivilstatus = SivilstandsdetaljerMapper.mapTilDto(søknadBarnetilsyn.sivilstandsdetaljer.verdi),
            medlemskap = MedlemsskapsMapper.mapTilDto(søknadBarnetilsyn.medlemskapsdetaljer.verdi),
            bosituasjon =
                BosituasjonMapper.mapTilDto(
                    søknadBarnetilsyn.bosituasjon.verdi,
                    søknadBarnetilsyn.sivilstandsplaner?.verdi,
                ),
            person = PersonTilGjenbruk(BarnMapper.mapTilDto(søknadBarnetilsyn.barn.verdi)),
            aktivitet = AktivitetsMapper.mapTilDto(søknadBarnetilsyn.aktivitet.verdi),
        )
    }
}
