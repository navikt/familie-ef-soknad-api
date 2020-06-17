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
                     innsendingMottatt: LocalDateTime): SøknadMedVedlegg {
        val vedlegg: Map<String, List<Vedlegg>> = hentDokumenter(frontendDto.dokumentasjonsbehov)

        val søknad = Søknad(
                innsendingsdetaljer = Søknadsfelt("Innsendingsdetaljer",
                                                  Innsendingsdetaljer(Søknadsfelt("Dato mottatt", innsendingMottatt))),
                personalia = Søknadsfelt("Søker", PersonaliaMapper.mapPersonalia(frontendDto)),
                sivilstandsdetaljer = Søknadsfelt("Detaljer om sivilstand",
                                                  SivilstandsdetaljerMapper.mapSivilstandsdetaljer(frontendDto, vedlegg)),
                medlemskapsdetaljer = Søknadsfelt("Opphold i Norge", MedlemsskapsMapper.mapMedlemskap(frontendDto)),
                bosituasjon = Søknadsfelt("Bosituasjonen din",
                                          BosituasjonMapper.mapBosituasjon(frontendDto.bosituasjon, vedlegg)),
                sivilstandsplaner = Søknadsfelt("Sivilstandsplaner",
                                                SivilstandsplanerMapper.mapSivilstandsplaner(frontendDto.bosituasjon)),
                barn = Søknadsfelt("Barn funnet i tps/folkeregisteret",
                                   BarnMapper.mapBarn(frontendDto.person.barn, vedlegg)),
                aktivitet = Søknadsfelt("Arbeid, utdanning og andre aktiviteter", AktivitetsMapper.map(frontendDto)),
                situasjon = Søknadsfelt("Mer om situasjonen din", SituasjonsMapper.mapSituasjon(frontendDto, vedlegg)),
                stønadsstart = Søknadsfelt("Når søker du stønad fra?", stønadsstart()))
        return SøknadMedVedlegg(søknad, vedlegg.values.flatten())
    }

    private fun hentDokumenter(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, List<Vedlegg>> {
        return dokumentasjonsbehov.associate {
            // it.id er dokumenttype/tittel, eks "Gift i utlandet"
            it.id to it.opplastedeVedlegg.map { dokumentFelt ->
                Vedlegg(id = dokumentFelt.dokumentId,
                        navn = dokumentFelt.navn,
                        tittel = dokumentFelt.label,
                        bytes = dokumentServiceService.hentVedlegg(dokumentFelt.dokumentId))
            }
        }
    }

    private fun stønadsstart() = Stønadsstart(Søknadsfelt("Fra måned", Month.AUGUST), Søknadsfelt("Fra år", 2018))


}
