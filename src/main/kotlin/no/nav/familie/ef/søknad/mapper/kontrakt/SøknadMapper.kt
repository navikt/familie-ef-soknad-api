package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Situasjon
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.tilLocalDateEllerNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.*
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SøknadMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(frontendDto: SøknadDto,
                     innsendingMottatt: LocalDateTime): SøknadMedVedlegg {
        val vedlegg: Map<String, DokumentasjonWrapper> = hentDokumenter(frontendDto.dokumentasjonsbehov)

        val søknad = Søknad(
                innsendingsdetaljer = Søknadsfelt("Innsendingsdetaljer",
                                                  Innsendingsdetaljer(Søknadsfelt("Dato mottatt", innsendingMottatt))),
                personalia = Søknadsfelt("Søker", PersonaliaMapper.mapPersonalia(frontendDto)),
                sivilstandsdetaljer = Søknadsfelt("Årsak til alene med barn",
                                                  SivilstandsdetaljerMapper.mapSivilstandsdetaljer(frontendDto, vedlegg)),
                medlemskapsdetaljer = Søknadsfelt("Opphold i Norge", MedlemsskapsMapper.mapMedlemskap(frontendDto)),
                bosituasjon = Søknadsfelt("Bosituasjonen din",
                                          BosituasjonMapper.mapBosituasjon(frontendDto.bosituasjon, vedlegg)),
                sivilstandsplaner = Søknadsfelt("Fremtidsplaner",
                                                SivilstandsplanerMapper.mapSivilstandsplaner(frontendDto.bosituasjon)),
                barn = Søknadsfelt("Barna dine",
                                   BarnMapper.mapBarn(frontendDto.person.barn, vedlegg)),
                aktivitet = Søknadsfelt("Arbeid, utdanning og andre aktiviteter", AktivitetsMapper.map(frontendDto, vedlegg)),
                situasjon = Søknadsfelt("Mer om situasjonen din", SituasjonsMapper.mapSituasjon(frontendDto, vedlegg)),
                stønadsstart = Søknadsfelt("Når søker du stønad fra?", stønadsstart(frontendDto.merOmDinSituasjon)))
        return SøknadMedVedlegg(søknad, vedlegg.values.map { it.vedlegg }.flatten())
    }

    private fun hentDokumenter(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, DokumentasjonWrapper> {
        return dokumentasjonsbehov.associate {
            // it.id er dokumenttype/tittel, eks "Gift i utlandet"
            val vedlegg = it.opplastedeVedlegg.map { dokumentFelt ->
                Vedlegg(id = dokumentFelt.dokumentId,
                        navn = dokumentFelt.navn,
                        tittel = it.label,
                        bytes = dokumentServiceService.hentVedlegg(dokumentFelt.dokumentId))
            }
            val harSendtInn = Søknadsfelt("Jeg har sendt inn denne dokumentasjonen til NAV tidligere", it.harSendtInn)
            it.id to DokumentasjonWrapper(it.label, harSendtInn, vedlegg)
        }
    }

    private fun stønadsstart(merOmDinSituasjon: Situasjon): Stønadsstart {
        val month = merOmDinSituasjon.søknadsdato?.tilLocalDateEllerNull()?.month
        val year = merOmDinSituasjon.søknadsdato?.tilLocalDateEllerNull()?.year
        return Stønadsstart(month?.let { Søknadsfelt("Fra måned", month) },
                            year?.let { Søknadsfelt("Fra år", year) },
                            merOmDinSituasjon.søkerFraBestemtMåned.tilSøknadsfelt())
    }


}
