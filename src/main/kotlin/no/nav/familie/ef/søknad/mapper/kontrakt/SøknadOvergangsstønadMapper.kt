package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.Språk
import no.nav.familie.ef.søknad.mapper.kontekst
import no.nav.familie.ef.søknad.mapper.kontrakt.StønadsstartMapper.mapStønadsstart
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.tilKontrakt
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.SøknadOvergangsstønad
import no.nav.familie.kontrakter.ef.søknad.validering.OvergangsstønadValidering
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadOvergangsstønadMapper {

    fun mapTilIntern(
        dto: SøknadOvergangsstønadDto,
        innsendingMottatt: LocalDateTime,
        skalHenteVedlegg: Boolean = true
    ): SøknadMedVedlegg<SøknadOvergangsstønad> {
        kontekst.set(Språk.fromString(dto.locale))
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)

        val søknad = SøknadOvergangsstønad(
            innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
            personalia = PersonaliaMapper.map(dto.person.søker),
            opplysningerOmAdresse = OpplysningerOmAdresseMapper.map(
                OpplysningerOmAdresseData(
                    dto.søkerBorPåRegistrertAdresse,
                    dto.opplysningerOmAdresse
                ), vedlegg
            ),
            sivilstandsdetaljer = SivilstandsdetaljerMapper.map(dto.sivilstatus, vedlegg),
            medlemskapsdetaljer = MedlemsskapsMapper.map(dto.medlemskap),
            bosituasjon = BosituasjonMapper.map(dto.bosituasjon, vedlegg),
            sivilstandsplaner = SivilstandsplanerMapper.map(dto.bosituasjon),
            barn = dto.person.barn.tilSøknadsfelt(vedlegg),
            aktivitet = AktivitetsMapper.map(dto.aktivitet, vedlegg),
            situasjon = SituasjonsMapper.map(dto, vedlegg),
            stønadsstart = mapStønadsstart(dto.merOmDinSituasjon.søknadsdato, dto.merOmDinSituasjon.søkerFraBestemtMåned)
        )

        OvergangsstønadValidering.validate(søknad)

        return SøknadMedVedlegg(
            søknad,
            vedlegg.values.flatMap { it.vedlegg },
            dto.dokumentasjonsbehov.tilKontrakt(),
            dto.skalBehandlesINySaksbehandling
        )
    }
}
