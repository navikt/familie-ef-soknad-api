package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadRegelendring2026Dto
import no.nav.familie.ef.søknad.søknad.mapper.AktivitetsMapper.mapOmFirmaer
import no.nav.familie.ef.søknad.søknad.mapper.StønadsstartMapper.mapStønadsstart
import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.Språk
import no.nav.familie.ef.søknad.utils.Språktekster.OmFirmaDuDriver
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.kontekst
import no.nav.familie.ef.søknad.utils.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.tilKontrakt
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.SøknadOvergangsstønadRegelendring2026
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadOvergangsstønadRegelendring2026Mapper {
    fun mapTilIntern(
        dto: SøknadOvergangsstønadRegelendring2026Dto,
        innsendingMottatt: LocalDateTime,
    ): SøknadMedVedlegg<SøknadOvergangsstønadRegelendring2026> {
        kontekst.set(Språk.fromString(dto.locale))
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(dto.dokumentasjonsbehov)

        val søknad =
            SøknadOvergangsstønadRegelendring2026(
                erRegelendring2026 = dto.erRegelendring2026,
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
                hvaSituasjon = dto.hvaSituasjon.tilSøknadsfelt(),
                inntekter = dto.inntekter.tilSøknadsfelt(),
                firmaer = dto.firmaer?.let { Søknadsfelt(OmFirmaDuDriver.hentTekst(), mapOmFirmaer(it)) },
                sagtOppEllerRedusertStilling = dto.sagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                begrunnelseSagtOppEllerRedusertStilling = dto.begrunnelseSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                datoSagtOppEllerRedusertStilling = dto.datoSagtOppEllerRedusertStilling?.tilSøknadsfelt(),
                stønadsstart = mapStønadsstart(dto.søknadsdato, dto.søkerFraBestemtMåned),
            )

        return SøknadMedVedlegg<SøknadOvergangsstønadRegelendring2026>(
            søknad = søknad,
            vedlegg = vedlegg.values.flatMap { it.vedlegg },
            dokumentasjonsbehov = dto.dokumentasjonsbehov.tilKontrakt(),
        )
    }
}
