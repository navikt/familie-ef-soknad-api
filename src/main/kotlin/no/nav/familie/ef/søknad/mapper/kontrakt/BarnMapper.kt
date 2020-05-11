package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDate
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder as AnnenForelderDto

/**
 * Forelder
 *      kanIkkeOppgiAnnenForelderFar/ikkeOppgittAnnenForelderBegrunnelse
 *          Burde det hete far? (funksjonellt)
 *      Land - Trenger vi denne hvis vi allerde vet om de bor i Norge/?
 *      adresse - Ok att vi sletter?
 *      Navn påkrevd hvis forelder finnes?
 *
 */
object BarnMapper {

    fun mapFolkeregistrerteBarn(barn: List<Barn>): List<RegistrertBarn> {
        return barn.filterNot { falseOrNull(it.lagtTil) }
                .map {
                    RegistrertBarn(navn = it.navn.tilSøknadsfelt(),
                                   fødselsnummer = it.fnr.tilSøknadsfelt(::Fødselsnummer),
                                   harSammeAdresse = it.harSammeAdresse.tilSøknadsfelt(),
                                   annenForelder = mapAnnenForelder(it.forelder),
                                   samvær = mapSamvær(it.forelder)
                    )
                }
    }

    fun mapNyttBarn(barn: List<Barn>): List<NyttBarn> {
        return barn.filter { falseOrNull(it.lagtTil) }
                .map {
                    NyttBarn(navn = it.navn.tilSøknadsfelt(),
                             fødselsnummer = it.fnr.tilSøknadsfelt(),
                             erBarnetFødt = it.født.tilSøknadsfelt(),
                             fødselTermindato = Søknadsfelt(it.fødselsdato.label, LocalDate.parse(it.fødselsdato.verdi)), //TODO
                            //terminbekreftelse =
                             skalBarnetBoHosSøker = it.skalBarnBoHosDeg.tilSøknadsfelt(),
                             annenForelder = mapAnnenForelder(it.forelder),
                             samvær = mapSamvær(it.forelder)
                    )
                }
    }

    private fun mapAnnenForelder(forelder: AnnenForelderDto): Søknadsfelt<AnnenForelder> =
            Søknadsfelt("Barnets andre forelder", AnnenForelder(
                    //kanIkkeOppgiAnnenForelderFar = forelder. //far?
                    //ikkeOppgittAnnenForelderBegrunnelse = forelder.begru
                    bosattNorge = forelder.borINorge?.tilSøknadsfelt(),
                    person = Søknadsfelt("Persondata", PersonMinimum(
                            fødselsnummer = forelder.personnr?.tilSøknadsfelt(::Fødselsnummer),
                            fødselsdato = forelder.fødselsdato?.tilSøknadsfelt(),
                            navn = forelder.navn.tilSøknadsfelt()
                    ))
            ))

    private fun mapSamvær(forelder: AnnenForelderDto): Søknadsfelt<Samvær> = Søknadsfelt("samvær", Samvær(
            spørsmålAvtaleOmDeltBosted = Søknadsfelt(forelder.avtaleOmDeltBosted.label,
                                                     forelder.avtaleOmDeltBosted.verdi),
            avtaleOmDeltBosted = dokumentfelt("Avtale om delt bosted for barna"), //TODO vedlegg
            skalAnnenForelderHaSamvær = forelder.harAnnenForelderSamværMedBarn?.tilSøknadsfelt(),
            harDereSkriftligAvtaleOmSamvær = forelder.harDereSkriftligSamværsavtale?.tilSøknadsfelt(),
            samværsavtale = dokumentfelt("Avtale om samvær"), //TODO vedlegg
            hvordanPraktiseresSamværet = forelder.hvordanPraktiseresSamværet?.tilSøknadsfelt(),
            borAnnenForelderISammeHus = forelder.borISammeHus?.let {
                Søknadsfelt(it.label, it.verdi == "ja")  // TODO gjøres om til String i kontrakter
            },
            harDereTidligereBoddSammen = forelder.boddSammenFør?.tilSøknadsfelt(),
            nårFlyttetDereFraHverandre = forelder.flyttetFra?.tilSøknadsfelt(),
            erklæringOmSamlivsbrudd = dokumentfelt(
                    "Erklæring om samlivsbrudd"), //TODO vedlegg
            hvorMyeErDuSammenMedAnnenForelder = forelder.hvorMyeSammen?.tilSøknadsfelt(),
            beskrivSamværUtenBarn = null // Dekkes denne av hvordanPraktiseresSamværet ? Fjerne fra kontrakt?
    ))

    private fun falseOrNull(it: Boolean?) = it ?: false

    fun dokumentfelt(tittel: String) = Søknadsfelt("Dokument", Dokument(byteArrayOf(12), tittel))

}
