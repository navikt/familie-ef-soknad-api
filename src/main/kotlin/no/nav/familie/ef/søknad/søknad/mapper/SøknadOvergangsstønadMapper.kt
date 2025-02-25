package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.infrastruktur.config.ApplicationConfig
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.søknad.mapper.StønadsstartMapper.mapStønadsstart
import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.Språk
import no.nav.familie.ef.søknad.utils.kontekst
import no.nav.familie.ef.søknad.utils.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.språk
import no.nav.familie.ef.søknad.utils.tilKontrakt
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.SøknadOvergangsstønad
import no.nav.familie.kontrakter.ef.søknad.validering.OvergangsstønadValidering
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadOvergangsstønadMapper {
    fun mapTilIntern(
        dto: SøknadOvergangsstønadDto,
        innsendingMottatt: LocalDateTime,
        skalHenteVedlegg: Boolean = true,
    ): SøknadMedVedlegg<SøknadOvergangsstønad> {
        kontekst.set(Språk.fromString(dto.locale))
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)

        logger.info("DEBUG MODE --- SøknadOvergangstønadDto sin locale er: ${dto.locale}")
        logger.info("DEBUG MODE --- Språkkontekst er: ${språk()}")

        val søknad =
            SøknadOvergangsstønad(
                innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt, dto.datoPåbegyntSøknad, dto.locale),
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
                barn = dto.person.barn.tilSøknadsfelt(vedlegg),
                aktivitet = AktivitetsMapper.map(dto.aktivitet, vedlegg),
                situasjon = SituasjonsMapper.map(dto, vedlegg),
                stønadsstart = mapStønadsstart(dto.merOmDinSituasjon.søknadsdato, dto.merOmDinSituasjon.søkerFraBestemtMåned),
            )

        OvergangsstønadValidering.validate(søknad)

        return SøknadMedVedlegg(
            søknad,
            vedlegg.values.flatMap { it.vedlegg },
            dto.dokumentasjonsbehov.tilKontrakt(),
            dto.skalBehandlesINySaksbehandling,
        )
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ApplicationConfig::class.java)
    }
}
