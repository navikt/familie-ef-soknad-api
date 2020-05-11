package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn
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
                    RegistrertBarn(navn = Søknadsfelt(it.navn.label, it.navn.verdi),
                                   fødselsnummer = Søknadsfelt(it.fnr.label, Fødselsnummer(it.fnr.verdi)),
                                   harSammeAdresse = Søknadsfelt(it.harSammeAdresse.label, it.harSammeAdresse.verdi),
                                   annenForelder = mapAnnenForelder(it.forelder),
                                   samvær = mapSamvær(it.forelder)
                    )
                }
    }

    fun mapNyttBarn(barn: List<Barn>): List<NyttBarn> {
        return barn.filter { falseOrNull(it.lagtTil) }
                .map {
                    NyttBarn(navn = Søknadsfelt(it.navn.label, it.navn.verdi),
                             fødselsnummer = Søknadsfelt(it.fnr.label, it.fnr.verdi),
                             erBarnetFødt = Søknadsfelt(it.født.label, it.født.verdi),
                             fødselTermindato = Søknadsfelt(it.fødselsdato.label, LocalDate.parse(it.fødselsdato.verdi)), //TODO
                            //terminbekreftelse =
                             skalBarnetBoHosSøker = Søknadsfelt(it.skalBarnBoHosDeg.label, it.skalBarnBoHosDeg.verdi),
                             annenForelder = mapAnnenForelder(it.forelder),
                             samvær = mapSamvær(it.forelder)
                    )
                }
    }

    private fun mapAnnenForelder(forelder: AnnenForelderDto): Søknadsfelt<AnnenForelder> =
            Søknadsfelt("Barnets andre forelder", AnnenForelder(
                    //kanIkkeOppgiAnnenForelderFar = forelder. //far?
                    //ikkeOppgittAnnenForelderBegrunnelse = forelder.begru
                    bosattNorge = forelder.borINorge?.let {
                        Søknadsfelt(it.label, it.verdi)
                    },
                    person = Søknadsfelt("Persondata", PersonMinimum(
                            fødselsnummer = forelder.personnr
                                    ?.let {
                                        Søknadsfelt(it.label, Fødselsnummer(it.verdi))
                                    },
                            fødselsdato = forelder.fødselsdato?.let {
                                Søknadsfelt(it.label, it.verdi)
                            },
                            navn = forelder.navn.let { Søknadsfelt(it.label, it.verdi) }
                    ))
            ))

    private fun mapSamvær(forelder: AnnenForelderDto): Søknadsfelt<Samvær> = Søknadsfelt("samvær", Samvær(
            spørsmålAvtaleOmDeltBosted = Søknadsfelt(forelder.avtaleOmDeltBosted.label,
                                                     forelder.avtaleOmDeltBosted.verdi),
            avtaleOmDeltBosted = dokumentfelt("Avtale om delt bosted for barna"), //TODO vedlegg
            skalAnnenForelderHaSamvær = forelder.harAnnenForelderSamværMedBarn?.let {
                Søknadsfelt(it.label, it.verdi)
            },
            harDereSkriftligAvtaleOmSamvær = forelder.harDereSkriftligSamværsavtale?.let {
                Søknadsfelt(it.label, it.verdi)
            },
            samværsavtale = dokumentfelt("Avtale om samvær"),//TODO vedlegg
            hvordanPraktiseresSamværet = forelder.hvordanPraktiseresSamværet?.let {
                Søknadsfelt(it.label, it.verdi)
            },
            borAnnenForelderISammeHus = forelder.borISammeHus?.let {
                Søknadsfelt(it.label, it.verdi == "ja") // TODO gjøres om til String i kontrakter
            },
            harDereTidligereBoddSammen = forelder.boddSammenFør?.let { Søknadsfelt(it.label, it.verdi) },
            nårFlyttetDereFraHverandre = forelder.flyttetFra?.let {
                Søknadsfelt(it.label, it.verdi)
            },
            erklæringOmSamlivsbrudd = dokumentfelt(
                    "Erklæring om samlivsbrudd"), //TODO vedlegg
            hvorMyeErDuSammenMedAnnenForelder = forelder.hvorMyeSammen?.let {
                Søknadsfelt(it.label, it.verdi)
            },
            beskrivSamværUtenBarn = null // Dekkes denne av hvordanPraktiseresSamværet ? Fjerne fra kontrakt?
    ))

    private fun falseOrNull(it: Boolean?) = it ?: false

    fun dokumentfelt(tittel: String) = Søknadsfelt("Dokument", Dokument(byteArrayOf(12), tittel))

}
