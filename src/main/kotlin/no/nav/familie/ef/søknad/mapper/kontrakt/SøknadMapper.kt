package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.VedleggFelt
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.*
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.Month

@Component
class SøknadMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(frontendDto: SøknadDto,
                     innsendingMottatt: LocalDateTime): Søknad {
        val dokumenter: Map<String, Dokument> = hentDokumenter(frontendDto.vedleggsliste)

        return Søknad(
                innsendingsdetaljer = Søknadsfelt("Innsendingsdetaljer",
                                                  Innsendingsdetaljer(Søknadsfelt("Dato mottatt", innsendingMottatt))),
                personalia = Søknadsfelt("Søker", PersonaliaMapper.mapPersonalia(frontendDto)),
                sivilstandsdetaljer = Søknadsfelt("Detaljer om sivilstand",
                                                  SivilstandsdetaljerMapper.mapSivilstandsdetaljer(frontendDto, dokumenter)),
                medlemskapsdetaljer = Søknadsfelt("Opphold i Norge", MedlemsskapsMapper.mapMedlemskap(frontendDto, dokumenter)),
                bosituasjon = Søknadsfelt("Bosituasjonen din", BosituasjonMapper.mapBosituasjon(frontendDto.bosituasjon)),
                sivilstandsplaner = Søknadsfelt("Sivilstandsplaner",
                                                SivilstandsplanerMapper.mapSivilstandsplaner(frontendDto.bosituasjon)),
                folkeregisterbarn = Søknadsfelt("Barn funnet i tps/folkeregisteret", BarnMapper.mapFolkeregistrerteBarn(frontendDto.person.barn)),
                kommendeBarn = Søknadsfelt("Barn lagt til", BarnMapper.mapNyttBarn(frontendDto.person.barn)),
                aktivitet = Søknadsfelt("Arbeid, utdanning og andre aktiviteter", AktivitetsMapper.map(frontendDto)),
                situasjon = Søknadsfelt("Mer om situasjonen din", SituasjonsMapper.mapSituasjon(frontendDto, dokumenter)),
                stønadsstart = Søknadsfelt("Når søker du stønad fra?", stønadsstart()))
    }

    private fun hentDokumenter(vedleggListe: List<VedleggFelt>): Map<String, Dokument> {
        return vedleggListe.associate { it.navn to Dokument(dokumentServiceService.hentVedlegg(it.dokumentId), it.label) }
    }

    private fun stønadsstart() = Stønadsstart(Søknadsfelt("Fra måned", Month.AUGUST), Søknadsfelt("Fra år", 2018))






}
