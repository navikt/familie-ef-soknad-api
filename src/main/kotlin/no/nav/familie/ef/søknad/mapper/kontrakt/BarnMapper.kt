package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn
import no.nav.familie.ef.søknad.mapper.falseOrNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDate
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder as AnnenForelderDto

/**
 * Forelder
 *          Burde det hete far? (funksjonellt)
 *      Land - Trenger vi denne hvis vi allerde vet om de bor i Norge/?
 *      adresse - Ok att vi sletter?
 *      Navn påkrevd hvis forelder finnes?
 *
 */
object BarnMapper {

    fun mapFolkeregistrerteBarn(barn: List<Barn>): List<RegistrertBarn> {
        return barn.filterNot { falseOrNull(it.lagtTil) }
                .map {barn ->
                    RegistrertBarn(navn = barn.navn.tilSøknadsfelt(),
                                   fødselsnummer = barn.fnr.tilSøknadsfelt(::Fødselsnummer),
                                   harSammeAdresse = barn.harSammeAdresse.tilSøknadsfelt(),
                                   annenForelder = mapAnnenForelder(barn.forelder),
                                   samvær = mapSamvær(barn.forelder)
                    )
                }
    }

    fun mapNyttBarn(barn: List<Barn>): List<NyttBarn> {
        return barn.filter { falseOrNull(it.lagtTil) }
                .map {barn ->
                    NyttBarn(navn = barn.navn.tilSøknadsfelt(),
                             fødselsnummer = barn.fnr.tilSøknadsfelt(),
                             erBarnetFødt = barn.født.tilSøknadsfelt(),
                             fødselTermindato = Søknadsfelt(barn.fødselsdato.label, LocalDate.parse(barn.fødselsdato.verdi)), // TODO Bedre måte?
                            //terminbekreftelse =
                             skalBarnetBoHosSøker = barn.skalBarnBoHosDeg?.tilSøknadsfelt() ?: error("Manglende påkrevd felt for nytt barn: skalBarnetBoHosSøker "), // TODO flytte til validering i kontrakter?
                             annenForelder = mapAnnenForelder(barn.forelder),
                             samvær = mapSamvær(barn.forelder)
                    )
                }
    }

    private fun mapAnnenForelder(forelder: AnnenForelderDto): Søknadsfelt<AnnenForelder> =
            Søknadsfelt("Barnets andre forelder", AnnenForelder(
                    kanIkkeOppgiAnnenForelderFar = forelder.kanIkkeOppgiAnnenForelderFar.tilSøknadsfelt(), //far? // @TODO Mangler i ui - Asbjørn legger til
                    ikkeOppgittAnnenForelderBegrunnelse = forelder.ikkeOppgittAnnenForelderBegrunnelse?.let {  it.tilSøknadsfelt() }, // @TODO Mangler i ui - Asbjørn legger til
                    bosattNorge = forelder.borINorge?.tilSøknadsfelt(),
                    // val land = forelder.land?.tilSøknadsfelt(), - venter på kontrakt oppdatering
                    person = Søknadsfelt("Persondata", PersonMinimum(
                            fødselsnummer = forelder.personnr?.tilSøknadsfelt(::Fødselsnummer),
                            fødselsdato = forelder.fødselsdato?.tilSøknadsfelt(),
                            navn = forelder.navn.tilSøknadsfelt()
                    )),
                    adresse = null // @TODO Mangler i ui - møte om dette med funksjonelle snart

            ))

    private fun mapSamvær(forelder: AnnenForelderDto): Søknadsfelt<Samvær> = Søknadsfelt("samvær", Samvær(
            spørsmålAvtaleOmDeltBosted = Søknadsfelt(forelder.avtaleOmDeltBosted.label,
                                                     forelder.avtaleOmDeltBosted.verdi),
            avtaleOmDeltBosted = dokumentfelt("Avtale om delt bosted for barna"), //TODO vedlegg
            skalAnnenForelderHaSamvær = forelder.harAnnenForelderSamværMedBarn?.tilSøknadsfelt(),
            harDereSkriftligAvtaleOmSamvær = forelder.harDereSkriftligSamværsavtale?.tilSøknadsfelt(),
            samværsavtale = dokumentfelt("Avtale om samvær"), //TODO vedlegg
            borAnnenForelderISammeHus = forelder.borISammeHus?.let {
                Søknadsfelt(it.label, it.verdi == "ja")  // TODO gjøres om til String i kontrakter
            },
            harDereTidligereBoddSammen = forelder.boddSammenFør?.tilSøknadsfelt(),
            nårFlyttetDereFraHverandre = forelder.flyttetFra?.tilSøknadsfelt(),
            erklæringOmSamlivsbrudd = dokumentfelt(
                    "Erklæring om samlivsbrudd"), //TODO vedlegg
            hvorMyeErDuSammenMedAnnenForelder = forelder.hvorMyeSammen?.tilSøknadsfelt(),
            hvordanPraktiseresSamværet = forelder.hvordanPraktiseresSamværet?.tilSøknadsfelt(),
            beskrivSamværUtenBarn = forelder.beskrivSamværUtenBarn?.let { it.tilSøknadsfelt() }  // @TODO Mangler i ui - Asbjørn legger til
    ))



    fun dokumentfelt(tittel: String) = Søknadsfelt("Dokument", Dokument(byteArrayOf(12), tittel))

}
