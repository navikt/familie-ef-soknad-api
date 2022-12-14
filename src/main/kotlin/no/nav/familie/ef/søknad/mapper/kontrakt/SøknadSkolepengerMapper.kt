package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadSkolepengerDto
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.Språk
import no.nav.familie.ef.søknad.mapper.kontekst
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.tilKontrakt
import no.nav.familie.kontrakter.ef.søknad.SkolepengerDokumentasjon
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.SøknadSkolepenger
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadSkolepengerMapper {

    fun mapTilIntern(
        dto: SøknadSkolepengerDto,
        innsendingMottatt: LocalDateTime,
        skalHenteVedlegg: Boolean = true
    ): SøknadMedVedlegg<SøknadSkolepenger> {
        kontekst.set(Språk.fromString(dto.locale))
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)
        val søknadSkolepenger = SøknadSkolepenger(
            innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt),
            personalia = PersonaliaMapper.map(dto.person.søker),
            adresseopplysninger = AdresseopplysningerMapper.map(
                AdresseopplysningerData(
                    dto.søkerBorPåRegistrertAdresse,
                    dto.adresseopplysninger
                ),
                vedlegg
            ),
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

        return SøknadMedVedlegg(
            søknadSkolepenger,
            vedlegg.values.flatMap { it.vedlegg },
            dto.dokumentasjonsbehov.tilKontrakt(),
            dto.skalBehandlesINySaksbehandling
        )
    }
}
