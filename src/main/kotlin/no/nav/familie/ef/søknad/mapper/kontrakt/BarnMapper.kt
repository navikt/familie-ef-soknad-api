package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.falseOrNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.*
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder as AnnenForelderDto

object BarnMapper {

    fun mapFolkeregistrerteBarn(barnliste: List<Barn>, dokumentMap: Map<String, Dokument>): List<RegistrertBarn> {
        return barnliste.filterNot { falseOrNull(it.lagtTil) }
                .map { barn ->
                    RegistrertBarn(
                            navn = barn.navn.tilSøknadsfelt(),
                            fødselsnummer = barn.fnr.tilSøknadsfelt(::Fødselsnummer),
                            harSammeAdresse = barn.harSammeAdresse.tilSøknadsfelt(),
                            annenForelder = mapAnnenForelder(barn.forelder),
                            samvær = mapSamvær(barn.forelder, dokumentMap),
                            ikkeRegistrertPåSøkersAdresseBeskrivelse = barn.ikkeRegistrertPåSøkersAdresseBeskrivelse?.tilSøknadsfelt()
                            )
                }
    }

    fun mapNyttBarn(barnliste: List<Barn>, dokumentMap: Map<String, Dokument>): List<NyttBarn> {
        return barnliste.filter { falseOrNull(it.lagtTil) }
                .map { barn ->
                    NyttBarn(
                            navn = barn.navn.tilSøknadsfelt(),
                            fødselsnummer = barn.fnr.tilSøknadsfelt(),
                            erBarnetFødt = barn.født.tilSøknadsfelt(),
                            fødselTermindato = barn.fødselsdato.tilSøknadsfelt(),
                            terminbekreftelse = dokumentfelt("Terminbekreftelse",
                                                             dokumentMap), //TODO vedlegg har ikke snakket om denne
                            skalBarnetBoHosSøker = barn.skalBarnBoHosDeg?.tilSøknadsfelt()
                                                   ?: error("Manglende påkrevd felt for nytt barnliste: skalBarnetBoHosSøker "),
                            annenForelder = mapAnnenForelder(barn.forelder),
                            samvær = mapSamvær(barn.forelder, dokumentMap)
                    )
                }
    }

    private fun mapAnnenForelder(forelder: AnnenForelderDto): Søknadsfelt<AnnenForelder> =
            Søknadsfelt("Barnets andre forelder", AnnenForelder(
                    kanIkkeOppgiAnnenForelderFar = forelder.kanIkkeOppgiAnnenForelderFar.tilSøknadsfelt(),
                    ikkeOppgittAnnenForelderBegrunnelse = forelder.ikkeOppgittAnnenForelderBegrunnelse?.tilSøknadsfelt(),
                    bosattNorge = forelder.borINorge?.tilSøknadsfelt(),
                    land = forelder.land?.tilSøknadsfelt(),
                    person = Søknadsfelt("Persondata", PersonMinimum(
                            fødselsnummer = forelder.personnr?.tilSøknadsfelt(::Fødselsnummer),
                            fødselsdato = forelder.fødselsdato?.tilSøknadsfelt(),
                            navn = forelder.navn.tilSøknadsfelt()
                    ))
            ))

    private fun mapSamvær(forelder: AnnenForelderDto,
                          dokumentMap: Map<String, Dokument>): Søknadsfelt<Samvær> = Søknadsfelt("samvær", Samvær(
            spørsmålAvtaleOmDeltBosted = forelder.avtaleOmDeltBosted.tilSøknadsfelt(),
            avtaleOmDeltBosted = dokumentfelt("Avtale om delt bosted for barna", dokumentMap), //TODO vedlegg
            skalAnnenForelderHaSamvær = forelder.harAnnenForelderSamværMedBarn?.tilSøknadsfelt(),
            harDereSkriftligAvtaleOmSamvær = forelder.harDereSkriftligSamværsavtale?.tilSøknadsfelt(),
            samværsavtale = dokumentfelt("Avtale om samvær", dokumentMap), //TODO vedlegg
            borAnnenForelderISammeHus = forelder.borAnnenForelderISammeHus?.tilSøknadsfelt(),
            borAnnenForelderISammeHusBeskrivelse = forelder.borAnnenForelderISammeHusBeskrivelse?.tilSøknadsfelt(),
            harDereTidligereBoddSammen = forelder.boddSammenFør?.tilSøknadsfelt(),
            nårFlyttetDereFraHverandre = forelder.flyttetFra?.tilSøknadsfelt(),
            erklæringOmSamlivsbrudd = dokumentfelt("Erklæring om samlivsbrudd", dokumentMap), //TODO vedlegg
            hvorMyeErDuSammenMedAnnenForelder = forelder.hvorMyeSammen?.tilSøknadsfelt(),
            // Ytterligere informasjon som innhentes dersom hvorMyeErDuSammenMedAnnenForelder =
            // "Vi møtes også utenom henting og levering" => (hvordanPraktiseresSamværet)
            hvordanPraktiseresSamværet = forelder.hvordanPraktiseresSamværet?.tilSøknadsfelt(),
            beskrivSamværUtenBarn = forelder.beskrivSamværUtenBarn?.tilSøknadsfelt()
    ))

}
