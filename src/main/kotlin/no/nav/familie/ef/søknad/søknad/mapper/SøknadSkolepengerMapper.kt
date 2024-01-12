package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.DokumentIdentifikator
import no.nav.familie.ef.søknad.søknad.dto.SøknadSkolepengerDto
import no.nav.familie.ef.søknad.søknad.tilSøknadsfelt
import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.utils.Språk
import no.nav.familie.ef.søknad.utils.kontekst
import no.nav.familie.ef.søknad.utils.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.tilKontrakt
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
        skalHenteVedlegg: Boolean = true,
    ): SøknadMedVedlegg<SøknadSkolepenger> {
        kontekst.set(Språk.fromString(dto.locale))
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)
        val søknadSkolepenger = SøknadSkolepenger(
            innsendingsdetaljer = FellesMapper.mapInnsendingsdetaljer(innsendingMottatt, dto.datoPåbegyntSøknad),
            personalia = PersonaliaMapper.map(dto.person.søker),
            adresseopplysninger = AdresseopplysningerMapper.map(
                AdresseopplysningerData(
                    dto.søkerBorPåRegistrertAdresse,
                    dto.adresseopplysninger,
                ),
                vedlegg,
            ),
            barn = dto.person.barn.tilSøknadsfelt(vedlegg),
            sivilstandsdetaljer = SivilstandsdetaljerMapper.map(dto.sivilstatus, vedlegg),
            medlemskapsdetaljer = MedlemsskapsMapper.map(dto.medlemskap),
            bosituasjon = BosituasjonMapper.map(dto.bosituasjon, vedlegg),
            sivilstandsplaner = SivilstandsplanerMapper.map(dto.bosituasjon),
            utdanning = UtdanningMapper.map(dto.utdanning),
            dokumentasjon = SkolepengerDokumentasjon(
                utdanningsutgifter = dokumentfelt(DokumentIdentifikator.UTGIFTER_UTDANNING, vedlegg),
            ),
        )

        return SøknadMedVedlegg(
            søknadSkolepenger,
            vedlegg.values.flatMap { it.vedlegg },
            dto.dokumentasjonsbehov.tilKontrakt(),
            dto.skalBehandlesINySaksbehandling,
        )
    }
}
