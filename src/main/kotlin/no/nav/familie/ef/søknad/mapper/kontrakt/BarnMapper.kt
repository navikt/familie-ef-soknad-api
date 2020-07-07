package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.*
import no.nav.familie.ef.søknad.mapper.tilSøknadsDatoFeltEllerNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.*
import org.slf4j.LoggerFactory
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder as AnnenForelderDto
import no.nav.familie.kontrakter.ef.søknad.Barn as Kontraktbarn

object BarnMapper {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun mapBarn(barnliste: List<Barn>, vedlegg: Map<String, DokumentasjonWrapper>): List<Kontraktbarn> {

        return barnliste.map { barn ->
            try {
                Kontraktbarn(navn = barn.navn?.tilSøknadsfelt(),
                             fødselsnummer = mapFødselsnummer(barn),
                             harSkalHaSammeAdresse = barn.harSammeAdresse.tilSøknadsfelt(),
                             ikkeRegistrertPåSøkersAdresseBeskrivelse = barn.ikkeRegistrertPåSøkersAdresseBeskrivelse
                                     ?.tilSøknadsfelt(),
                             erBarnetFødt = barn.født.tilSøknadsfelt(),
                             fødselTermindato = barn.fødselsdato?.tilSøknadsDatoFeltEllerNull(),
                             terminbekreftelse = dokumentfelt(TERMINBEKREFTELSE, vedlegg),
                             annenForelder = mapAnnenForelder(barn.forelder),
                             samvær = mapSamvær(barn.forelder, vedlegg))
            } catch (e: Exception) {
                secureLogger.error("Feil ved mapping av barn: $barnliste")
                throw e
            }
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
                    ikkeOppgittAnnenForelderBegrunnelse = forelder.ikkeOppgittAnnenForelderBegrunnelse?.tilSøknadsfelt(),
                    bosattNorge = forelder.borINorge?.tilSøknadsfelt(),
                    land = forelder.land?.tilSøknadsfelt(),
                    person = PersonMinimumMapper.map(forelder)
            ))

    private fun mapSamvær(forelder: AnnenForelderDto,
                          dokumentMap: Map<String, DokumentasjonWrapper>): Søknadsfelt<Samvær> = Søknadsfelt("samvær", Samvær(
            spørsmålAvtaleOmDeltBosted = forelder.avtaleOmDeltBosted?.tilSøknadsfelt(),
            avtaleOmDeltBosted = dokumentfelt(DELT_BOSTED, dokumentMap),
            skalAnnenForelderHaSamvær = forelder.harAnnenForelderSamværMedBarn?.tilSøknadsfelt(),
            harDereSkriftligAvtaleOmSamvær = forelder.harDereSkriftligSamværsavtale?.tilSøknadsfelt(),
            samværsavtale = dokumentfelt(SAMVÆRSAVTALE, dokumentMap),
            borAnnenForelderISammeHus = forelder.borAnnenForelderISammeHus?.tilSøknadsfelt(),
            borAnnenForelderISammeHusBeskrivelse = forelder.borAnnenForelderISammeHusBeskrivelse?.tilSøknadsfelt(),
            harDereTidligereBoddSammen = forelder.boddSammenFør?.tilSøknadsfelt(),
            nårFlyttetDereFraHverandre = forelder.flyttetFra?.tilSøknadsfelt(),
            hvorMyeErDuSammenMedAnnenForelder = forelder.hvorMyeSammen?.tilSøknadsfelt(),
            // Ytterligere informasjon som innhentes dersom hvorMyeErDuSammenMedAnnenForelder =
            // "Vi møtes også utenom henting og levering" => (hvordanPraktiseresSamværet)
            hvordanPraktiseresSamværet = forelder.hvordanPraktiseresSamværet?.tilSøknadsfelt(),
            beskrivSamværUtenBarn = forelder.beskrivSamværUtenBarn?.tilSøknadsfelt(),
            skalBarnetBoHosSøkerMenAnnenForelderSamarbeiderIkke = dokumentfelt(BARN_BOR_HOS_SØKER, dokumentMap)
    ))

}
