package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.*
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.Month

@Component
class SøknadMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(frontendDto: SøknadDto,
                     innsendingMottatt: LocalDateTime): Søknad {
        val dokumenter: Map<String, List<Dokument>> = hentDokumenter(frontendDto.dokumentasjonsbehov)

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
                barn = Søknadsfelt("Barn funnet i tps/folkeregisteret",
                                   BarnMapper.mapBarn(frontendDto.person.barn, dokumenter)),
                aktivitet = Søknadsfelt("Arbeid, utdanning og andre aktiviteter", AktivitetsMapper.map(frontendDto)),
                situasjon = Søknadsfelt("Mer om situasjonen din", SituasjonsMapper.mapSituasjon(frontendDto, dokumenter)),
                stønadsstart = Søknadsfelt("Når søker du stønad fra?", stønadsstart()))
    }

    private fun hentDokumenter(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, List<Dokument>> {
        return dokumentasjonsbehov.associate {
            it.id to it.opplastedeVedlegg.map { dokumentFelt ->
                Dokument(dokumentServiceService.hentVedlegg(dokumentFelt.dokumentId), dokumentFelt.label)
            }
        }

    }

    private fun stønadsstart() = Stønadsstart(Søknadsfelt("Fra måned", Month.AUGUST), Søknadsfelt("Fra år", 2018))


}
