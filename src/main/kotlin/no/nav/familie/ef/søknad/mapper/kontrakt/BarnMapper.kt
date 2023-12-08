package no.nav.familie.ef.søknad.mapper.kontrakt

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.Språktekster
import no.nav.familie.ef.søknad.mapper.Språktekster.BarnaDine
import no.nav.familie.ef.søknad.mapper.fødselsnummerTilTekstFelt
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.BARN_BOR_HOS_SØKER
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.DELT_BOSTED
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.SAMVÆRSAVTALE
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.TERMINBEKREFTELSE
import no.nav.familie.ef.søknad.mapper.tilBooleanFelt
import no.nav.familie.ef.søknad.mapper.tilDatoFelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsDatoFeltEllerNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.ef.søknad.mapper.tilTekstFelt
import no.nav.familie.kontrakter.ef.søknad.AnnenForelder
import no.nav.familie.kontrakter.ef.søknad.Samvær
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.felles.Fødselsnummer
import java.time.LocalDate
import java.time.Period
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder as AnnenForelderDto
import no.nav.familie.ef.søknad.mapper.Språktekster.Fødselsnummer as FødselsnummerTekst
import no.nav.familie.ef.søknad.mapper.Språktekster.Alder as AlderTekst
import no.nav.familie.kontrakter.ef.søknad.Barn as Kontraktbarn

object BarnMapper : MapperMedVedlegg<List<Barn>, List<Kontraktbarn>>(BarnaDine) {
    val manglerAnnenForelderTeller: Counter = Metrics.counter("alene.med.barn.soknad.manglerMedforelder")

    init {
        manglerAnnenForelderTeller.count() // For å initialisere telleren til 0 ved første søknad etter oppstart
    }

    override fun mapDto(data: List<Barn>, vedlegg: Map<String, DokumentasjonWrapper>): List<Kontraktbarn> {
        return data.map { barn ->
            tilKontraktBarn(barn, vedlegg)
        }
    }

    fun mapTilDto(barn: List<Kontraktbarn>): List<Barn> {
        return barn.map {
            Barn(
                alder = TekstFelt(AlderTekst.hentTekst(), Period.between(it.fødselsnummer?.verdi?.fødselsdato, LocalDate.now()).years.toString()),
                ident = TekstFelt(FødselsnummerTekst.hentTekst(), it.fødselsnummer?.verdi?.verdi ?: ""),
                fødselsdato = it.fødselTermindato.tilDatoFelt(),
                harSammeAdresse = BooleanFelt(it.harSkalHaSammeAdresse.label, it.harSkalHaSammeAdresse.verdi),
                ikkeRegistrertPåSøkersAdresseBeskrivelse = it.ikkeRegistrertPåSøkersAdresseBeskrivelse.tilTekstFelt(),
                id = null,
                lagtTil = it.lagtTilManuelt,
                navn = it.navn.tilTekstFelt(),
                født = BooleanFelt(it.erBarnetFødt.label, it.erBarnetFødt.verdi),
                forelder = mapAnnenForelderOgSamværTilDto(it.annenForelder?.verdi, it.samvær?.verdi, it.skalBarnetBoHosSøker),
                skalHaBarnepass = null,
                særligeTilsynsbehov = it.særligeTilsynsbehov.tilTekstFelt(),
                barnepass = null,
            )
        }
    }

    private fun tilKontraktBarn(
        barn: Barn,
        vedlegg: Map<String, DokumentasjonWrapper>,
    ) =
        Kontraktbarn(
            navn = barn.navn?.tilSøknadsfelt(),
            fødselsnummer = mapFødselsnummer(barn),
            harSkalHaSammeAdresse = barn.harSammeAdresse.tilSøknadsfelt(),
            ikkeRegistrertPåSøkersAdresseBeskrivelse = barn.ikkeRegistrertPåSøkersAdresseBeskrivelse
                ?.tilSøknadsfelt(),
            erBarnetFødt = barn.født.tilSøknadsfelt(),
            fødselTermindato = barn.fødselsdato?.tilSøknadsDatoFeltEllerNull(),
            terminbekreftelse = dokumentfelt(TERMINBEKREFTELSE, vedlegg),
            annenForelder = barn.forelder?.let { mapAnnenForelder(it) },
            samvær = barn.forelder?.let { mapSamvær(it, vedlegg) },
            skalHaBarnepass = barn.skalHaBarnepass?.tilSøknadsfelt(),
            særligeTilsynsbehov = barn.særligeTilsynsbehov?.tilSøknadsfelt(),
            barnepass = barn.barnepass?.let { BarnepassMapper.map(it) },
            lagtTilManuelt = barn.lagtTil,
            skalBarnetBoHosSøker = barn.forelder?.skalBarnetBoHosSøker?.tilSøknadsfelt(),
        )

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
        Søknadsfelt(
            "Barnets andre forelder",
            AnnenForelder(
                ikkeOppgittAnnenForelderBegrunnelse = forelder.ikkeOppgittAnnenForelderBegrunnelse?.tilSøknadsfelt(),
                bosattNorge = forelder.borINorge?.tilSøknadsfelt(),
                land = forelder.land?.tilSøknadsfelt(),
                person = PersonMinimumMapper.map(forelder),
            ),
        )

    private fun mapAnnenForelderOgSamværTilDto(annenForelder: AnnenForelder?, samvær: Samvær?, skalBarnetBoHosSøker: Søknadsfelt<String>?): AnnenForelderDto? {
        if (annenForelder == null) return null
        return AnnenForelderDto(
            ikkeOppgittAnnenForelderBegrunnelse = annenForelder.ikkeOppgittAnnenForelderBegrunnelse.tilTekstFelt(),
            navn = annenForelder.person?.verdi?.navn.tilTekstFelt(),
            fødselsdato = annenForelder.person?.verdi?.fødselsdato.tilDatoFelt(),
            ident = annenForelder.person?.verdi?.fødselsnummer.fødselsnummerTilTekstFelt(),
            borINorge = annenForelder.bosattNorge.tilBooleanFelt(),
            land = annenForelder.land.tilTekstFelt(),
            avtaleOmDeltBosted = samvær?.spørsmålAvtaleOmDeltBosted.tilBooleanFelt(),
            harAnnenForelderSamværMedBarn = samvær?.skalAnnenForelderHaSamvær.tilTekstFelt(),
            harDereSkriftligSamværsavtale = samvær?.harDereSkriftligAvtaleOmSamvær.tilTekstFelt(),
            hvordanPraktiseresSamværet = samvær?.hvordanPraktiseresSamværet.tilTekstFelt(),
            borAnnenForelderISammeHus = samvær?.borAnnenForelderISammeHus.tilTekstFelt(),
            borAnnenForelderISammeHusBeskrivelse = samvær?.borAnnenForelderISammeHusBeskrivelse.tilTekstFelt(),
            boddSammenFør = samvær?.harDereTidligereBoddSammen.tilBooleanFelt(),
            flyttetFra = samvær?.nårFlyttetDereFraHverandre.tilDatoFelt(),
            hvorMyeSammen = samvær?.hvorMyeErDuSammenMedAnnenForelder.tilTekstFelt(),
            beskrivSamværUtenBarn = samvær?.beskrivSamværUtenBarn.tilTekstFelt(),
            skalBarnetBoHosSøker = skalBarnetBoHosSøker.tilTekstFelt(),
        )
    }

    private fun mapSamvær(
        forelder: AnnenForelderDto,
        dokumentMap: Map<String, DokumentasjonWrapper>,
    ): Søknadsfelt<Samvær> = Søknadsfelt(
        Språktekster.Samvær.hentTekst(),
        Samvær(
            spørsmålAvtaleOmDeltBosted = forelder.avtaleOmDeltBosted?.tilSøknadsfelt(),
            avtaleOmDeltBosted = dokumentfelt(
                DELT_BOSTED,
                dokumentMap,
            ),
            skalAnnenForelderHaSamvær = forelder.harAnnenForelderSamværMedBarn?.tilSøknadsfelt(),
            harDereSkriftligAvtaleOmSamvær = forelder.harDereSkriftligSamværsavtale?.tilSøknadsfelt(),
            samværsavtale = dokumentfelt(
                SAMVÆRSAVTALE,
                dokumentMap,
            ),
            borAnnenForelderISammeHus = forelder.borAnnenForelderISammeHus?.tilSøknadsfelt(),
            borAnnenForelderISammeHusBeskrivelse = forelder.borAnnenForelderISammeHusBeskrivelse?.tilSøknadsfelt(),
            harDereTidligereBoddSammen = forelder.boddSammenFør?.tilSøknadsfelt(),
            nårFlyttetDereFraHverandre = forelder.flyttetFra?.tilSøknadsfelt(),
            hvorMyeErDuSammenMedAnnenForelder = forelder.hvorMyeSammen?.tilSøknadsfelt(),
            // Ytterligere informasjon som innhentes dersom hvorMyeErDuSammenMedAnnenForelder =
            // "Vi møtes også utenom henting og levering" => (hvordanPraktiseresSamværet)
            hvordanPraktiseresSamværet = forelder.hvordanPraktiseresSamværet?.tilSøknadsfelt(),
            beskrivSamværUtenBarn = forelder.beskrivSamværUtenBarn?.tilSøknadsfelt(),
            skalBarnetBoHosSøkerMenAnnenForelderSamarbeiderIkke = dokumentfelt(
                BARN_BOR_HOS_SØKER,
                dokumentMap,
            ),
        ),
    )
}
