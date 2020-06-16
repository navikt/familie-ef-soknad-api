package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.*
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder as AnnenForelderDto
import no.nav.familie.kontrakter.ef.søknad.Barn as Kontraktbarn

object BarnMapper {

    fun mapBarn(barnliste: List<Barn>, dokumentMap: Map<String, List<Dokument>>): List<Kontraktbarn> {
        return barnliste.map { barn ->
            Kontraktbarn(navn = barn.navn.tilSøknadsfelt(),
                         fødselsnummer = mapFødselsnummer(barn),
                         harSkalHaSammeAdresse = barn.harSammeAdresse.tilSøknadsfelt(),
                         ikkeRegistrertPåSøkersAdresseBeskrivelse = barn.ikkeRegistrertPåSøkersAdresseBeskrivelse
                                 ?.tilSøknadsfelt(),
                         erBarnetFødt = barn.født.tilSøknadsfelt(),
                         fødselTermindato = barn.fødselsdato.tilSøknadsfelt(),
                         terminbekreftelse = dokumentfelt(TERMINBEKREFTELSE, dokumentMap),
                         annenForelder = mapAnnenForelder(barn.forelder),
                         samvær = mapSamvær(barn.forelder, dokumentMap)
            )
        }
    }

    private fun mapFødselsnummer(barn: Barn): Søknadsfelt<Fødselsnummer>? {
        return barn.fnr?.let {
            return if (it.verdi.isNotBlank()) {
                barn.fnr.tilSøknadsfelt(::Fødselsnummer)
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

// TODO kan vurdere å legge på en let rundt person dersom kanIkkeOppgiAnnenForelderFar = true
                    person = Søknadsfelt("Persondata", PersonMinimum(
                            fødselsnummer = forelder.personnr?.tilSøknadsfelt(::Fødselsnummer),
                            fødselsdato = forelder.fødselsdato?.tilSøknadsfelt(),
                            navn = forelder.navn?.tilSøknadsfelt() ?: Søknadsfelt("Annen forelder navn",
                                                                                  "ikke oppgitt") // TODO dette må vi finne ut av, bør navn være optional i kontrakt
                    ))
            ))

    private fun mapSamvær(forelder: AnnenForelderDto,
                          dokumentMap: Map<String, List<Dokument>>): Søknadsfelt<Samvær> = Søknadsfelt("samvær", Samvær(
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
