package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.*
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder as AnnenForelderDto
import no.nav.familie.kontrakter.ef.søknad.Barn as Kontraktbarn

object BarnMapper {

    fun mapBarn(barnliste: List<Barn>, vedlegg: Map<String, List<Vedlegg>>): List<Kontraktbarn> {
        return barnliste.map { barn ->
            Kontraktbarn(navn = barn.navn.tilSøknadsfelt(),
                         fødselsnummer = mapFødselsnummer(barn),
                         harSkalHaSammeAdresse = barn.harSammeAdresse.tilSøknadsfelt(),
                         ikkeRegistrertPåSøkersAdresseBeskrivelse = barn.ikkeRegistrertPåSøkersAdresseBeskrivelse
                                 ?.tilSøknadsfelt(),
                         erBarnetFødt = barn.født.tilSøknadsfelt(),
                         fødselTermindato = barn.fødselsdato?.tilSøknadsfelt(),
                         terminbekreftelse = dokumentfelt(TERMINBEKREFTELSE, vedlegg),
                         annenForelder = mapAnnenForelder(barn.forelder),
                         samvær = mapSamvær(barn.forelder, vedlegg)
            )
        }
    }

    private fun mapFødselsnummer(barn: Barn): Søknadsfelt<Fødselsnummer>? {
        return barn.ident?.let {
            return if (it.verdi.isNotBlank()) {
                barn.ident.tilSøknadsfelt(::Fødselsnummer)
            } else {
                null
            }
        }

    }

    private fun mapAnnenForelder(forelder: AnnenForelderDto): Søknadsfelt<AnnenForelder> =
            Søknadsfelt("Barnets andre forelder", AnnenForelder(
                    kanIkkeOppgiAnnenForelderFar = forelder.kanIkkeOppgiAnnenForelderFar.tilSøknadsfelt(),
                    ikkeOppgittAnnenForelderBegrunnelse = forelder.ikkeOppgittAnnenForelderBegrunnelse?.tilSøknadsfelt(),
                    bosattNorge = forelder.borINorge?.tilSøknadsfelt(),
                    land = forelder.land?.tilSøknadsfelt(),
                    person = PersonMinimumMapper.map(forelder)
            ))

    private fun mapSamvær(forelder: AnnenForelderDto,
                          dokumentMap: Map<String, List<Vedlegg>>): Søknadsfelt<Samvær> = Søknadsfelt("samvær", Samvær(
            spørsmålAvtaleOmDeltBosted = forelder.avtaleOmDeltBosted?.tilSøknadsfelt(),
            avtaleOmDeltBosted = dokumentfelt(DELT_BOSTED, dokumentMap),
            skalAnnenForelderHaSamvær = forelder.harAnnenForelderSamværMedBarn?.tilSøknadsfelt(),
            harDereSkriftligAvtaleOmSamvær = forelder.harDereSkriftligSamværsavtale?.tilSøknadsfelt(),
            samværsavtale = dokumentfelt(SAMVÆRSAVTALE, dokumentMap),
            borAnnenForelderISammeHus = forelder.borAnnenForelderISammeHus?.tilSøknadsfelt(),
            borAnnenForelderISammeHusBeskrivelse = forelder.borAnnenForelderISammeHusBeskrivelse?.tilSøknadsfelt(),
            harDereTidligereBoddSammen = forelder.boddSammenFør?.tilSøknadsfelt(),
            nårFlyttetDereFraHverandre = forelder.flyttetFra?.tilSøknadsfelt(),
            erklæringOmSamlivsbrudd = dokumentfelt(SAMLIVSBRUDD, dokumentMap),
            hvorMyeErDuSammenMedAnnenForelder = forelder.hvorMyeSammen?.tilSøknadsfelt(),
            // Ytterligere informasjon som innhentes dersom hvorMyeErDuSammenMedAnnenForelder =
            // "Vi møtes også utenom henting og levering" => (hvordanPraktiseresSamværet)
            hvordanPraktiseresSamværet = forelder.hvordanPraktiseresSamværet?.tilSøknadsfelt(),
            beskrivSamværUtenBarn = forelder.beskrivSamværUtenBarn?.tilSøknadsfelt(),
            skalBarnetBoHosSøkerMenAnnenForelderSamarbeiderIkke = dokumentfelt(BARN_BOR_HOS_SØKER, dokumentMap)
    ))

}
