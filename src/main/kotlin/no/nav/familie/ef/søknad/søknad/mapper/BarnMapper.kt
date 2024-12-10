package no.nav.familie.ef.søknad.søknad.mapper

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import no.nav.familie.ef.søknad.person.mapper.PersonMinimumMapper
import no.nav.familie.ef.søknad.søknad.domain.Barn
import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.SAMVÆRSAVTALE
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.TERMINBEKREFTELSE
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.BARN_BOR_HOS_SØKER
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.utils.Språktekster
import no.nav.familie.ef.søknad.utils.Språktekster.BarnaDine
import no.nav.familie.ef.søknad.utils.fødselsnummerTilTekstFelt
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.tilNullableBooleanFelt
import no.nav.familie.ef.søknad.utils.tilNullableDatoFelt
import no.nav.familie.ef.søknad.utils.tilNullableTekstFelt
import no.nav.familie.ef.søknad.utils.tilSøknadsDatoFeltEllerNull
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.AnnenForelder
import no.nav.familie.kontrakter.ef.søknad.Samvær
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.felles.Fødselsnummer
import java.time.LocalDate
import java.time.Period
import java.util.UUID
import no.nav.familie.ef.søknad.søknad.domain.AnnenForelder as AnnenForelderDto
import no.nav.familie.ef.søknad.utils.Språktekster.Alder as AlderTekst
import no.nav.familie.ef.søknad.utils.Språktekster.Fødselsnummer as FødselsnummerTekst
import no.nav.familie.kontrakter.ef.søknad.Barn as Søknadbarn

object BarnMapper : MapperMedVedlegg<List<Barn>, List<Søknadbarn>>(BarnaDine) {
    val manglerAnnenForelderTeller: Counter = Metrics.counter("alene.med.barn.soknad.manglerMedforelder")

    init {
        manglerAnnenForelderTeller.count() // For å initialisere telleren til 0 ved første søknad etter oppstart
    }

    override fun mapDto(
        data: List<Barn>,
        vedlegg: Map<String, DokumentasjonWrapper>,
    ): List<Søknadbarn> =
        data.map { barn ->
            tilKontraktBarn(barn, vedlegg)
        }

    fun mapTilDto(barn: List<Søknadbarn>): List<Barn> =
        barn.map {
            Barn(
                alder =
                    TekstFelt(
                        AlderTekst.hentTekst(),
                        it.hentPdlAlder(),
                    ),
                ident = TekstFelt(FødselsnummerTekst.hentTekst(), it.fødselsnummer?.verdi?.verdi ?: ""),
                fødselsdato = it.fødselTermindato.tilNullableDatoFelt(),
                harSammeAdresse = BooleanFelt(it.harSkalHaSammeAdresse.label, it.harSkalHaSammeAdresse.verdi),
                ikkeRegistrertPåSøkersAdresseBeskrivelse = it.ikkeRegistrertPåSøkersAdresseBeskrivelse.tilNullableTekstFelt(),
                id = UUID.randomUUID().toString(),
                lagtTil = it.lagtTilManuelt,
                navn = it.navn.tilNullableTekstFelt(),
                født = BooleanFelt(it.erBarnetFødt.label, it.erBarnetFødt.verdi),
                forelder =
                    mapAnnenForelderOgSamværTilDto(
                        it.annenForelder?.verdi,
                        it.samvær?.verdi,
                        it.skalBarnetBoHosSøker,
                    ),
                skalHaBarnepass = it.skalHaBarnepass.tilNullableBooleanFelt(),
                særligeTilsynsbehov = it.særligeTilsynsbehov.tilNullableTekstFelt(),
                barnepass = null,
            )
        }

    private fun tilKontraktBarn(
        barn: Barn,
        vedlegg: Map<String, DokumentasjonWrapper>,
    ) = Søknadbarn(
        navn = barn.navn?.tilSøknadsfelt(),
        fødselsnummer = mapFødselsnummer(barn),
        harSkalHaSammeAdresse = barn.harSammeAdresse.tilSøknadsfelt(),
        ikkeRegistrertPåSøkersAdresseBeskrivelse =
            barn.ikkeRegistrertPåSøkersAdresseBeskrivelse
                ?.tilSøknadsfelt(),
        erBarnetFødt = barn.født.tilSøknadsfelt(),
        fødselTermindato = barn.fødselsdato?.tilSøknadsDatoFeltEllerNull(),
        terminbekreftelse = mapTerminbekreftelse(barn, vedlegg),
        annenForelder = barn.forelder?.let { mapAnnenForelder(it) },
        samvær = barn.forelder?.let { mapSamvær(it, vedlegg) },
        skalHaBarnepass = barn.skalHaBarnepass?.tilSøknadsfelt(),
        særligeTilsynsbehov = barn.særligeTilsynsbehov?.tilSøknadsfelt(),
        barnepass = barn.barnepass?.let { BarnepassMapper.map(it) },
        lagtTilManuelt = barn.lagtTil,
        skalBarnetBoHosSøker = barn.forelder?.skalBarnetBoHosSøker?.tilSøknadsfelt(),
    )

    private fun mapTerminbekreftelse(
        barn: Barn,
        vedlegg: Map<String, DokumentasjonWrapper>,
    ) = if (!barn.født.verdi) dokumentfelt(TERMINBEKREFTELSE, vedlegg) else null

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

    private fun mapAnnenForelderOgSamværTilDto(
        annenForelder: AnnenForelder?,
        samvær: Samvær?,
        skalBarnetBoHosSøker: Søknadsfelt<String>?,
    ): AnnenForelderDto? {
        if (annenForelder == null) return null
        return AnnenForelderDto(
            kanIkkeOppgiAnnenForelderFar =
                BooleanFelt(
                    "Jeg kan ikke oppgi den andre forelderen?",
                    annenForelder.ikkeOppgittAnnenForelderBegrunnelse?.verdi.erIkkeBlankEllerNull(),
                ),
            hvorforIkkeOppgi =
                hvorforIkkeOppgiTilTekstfeltEllerNullBasertPåBegrunnelse(
                    annenForelder.ikkeOppgittAnnenForelderBegrunnelse?.verdi,
                ),
            ikkeOppgittAnnenForelderBegrunnelse =
                annenForelder.ikkeOppgittAnnenForelderBegrunnelse?.let {
                    TekstFelt(
                        "Hvorfor kan du ikke oppgi den andre forelderen?",
                        it.verdi,
                    )
                },
            navn = lagNullableTekstfeltAvNavnHvisHvorforIkkeOppgiManglerVerdi(annenForelder),
            fødselsdato =
                annenForelder.person
                    ?.verdi
                    ?.fødselsdato
                    .tilNullableDatoFelt(),
            ident =
                annenForelder.person
                    ?.verdi
                    ?.fødselsnummer
                    .fødselsnummerTilTekstFelt(),
            borINorge = annenForelder.bosattNorge.tilNullableBooleanFelt(),
            land = annenForelder.land.tilNullableTekstFelt(),
            harAnnenForelderSamværMedBarn = samvær?.skalAnnenForelderHaSamvær.tilNullableTekstFelt(),
            harDereSkriftligSamværsavtale = samvær?.harDereSkriftligAvtaleOmSamvær.tilNullableTekstFelt(),
            hvordanPraktiseresSamværet = samvær?.hvordanPraktiseresSamværet.tilNullableTekstFelt(),
            borAnnenForelderISammeHus = samvær?.borAnnenForelderISammeHus.tilNullableTekstFelt(),
            borAnnenForelderISammeHusBeskrivelse = samvær?.borAnnenForelderISammeHusBeskrivelse.tilNullableTekstFelt(),
            boddSammenFør = samvær?.harDereTidligereBoddSammen.tilNullableBooleanFelt(),
            flyttetFra = samvær?.nårFlyttetDereFraHverandre.tilNullableDatoFelt(),
            hvorMyeSammen = samvær?.hvorMyeErDuSammenMedAnnenForelder.tilNullableTekstFelt(),
            beskrivSamværUtenBarn = samvær?.beskrivSamværUtenBarn.tilNullableTekstFelt(),
            skalBarnetBoHosSøker = skalBarnetBoHosSøker.tilNullableTekstFelt(),
        )
    }

    private fun hvorforIkkeOppgiBasertPåBegrunnelse(begrunnelse: String?): String? =
        when {
            begrunnelse == "Donor" -> "Donor"
            begrunnelse.erIkkeBlankEllerNull() -> "Annet"
            else -> null
        }

    private fun hvorforIkkeOppgiSvarIdBasertPåBegrunnelse(begrunnelse: String?): String? =
        when {
            begrunnelse == "Donor" -> "donorbarn"
            begrunnelse.erIkkeBlankEllerNull() -> "annet"
            else -> null
        }

    private fun String?.erIkkeBlankEllerNull() = !this.isNullOrBlank()

    private fun hvorforIkkeOppgiTilTekstfeltEllerNullBasertPåBegrunnelse(begrunnelse: String?): TekstFelt? {
        val hvorforIkkeOppgi = hvorforIkkeOppgiBasertPåBegrunnelse(begrunnelse)
        val svarId = hvorforIkkeOppgiSvarIdBasertPåBegrunnelse(begrunnelse)
        if (hvorforIkkeOppgi != null && svarId != null) {
            return TekstFelt("Hvorfor kan du ikke oppgi den andre forelderen?", hvorforIkkeOppgi, svarId)
        }
        return null
    }

    private fun lagNullableTekstfeltAvNavnHvisHvorforIkkeOppgiManglerVerdi(annenForelder: AnnenForelder?): TekstFelt? =
        if (annenForelder?.ikkeOppgittAnnenForelderBegrunnelse?.verdi != null) {
            null
        } else {
            annenForelder
                ?.person
                ?.verdi
                ?.navn
                .tilNullableTekstFelt()
        }

    private fun mapSamvær(
        forelder: AnnenForelderDto,
        dokumentMap: Map<String, DokumentasjonWrapper>,
    ): Søknadsfelt<Samvær> =
        Søknadsfelt(
            Språktekster.Samvær.hentTekst(),
            Samvær(
                skalAnnenForelderHaSamvær = forelder.harAnnenForelderSamværMedBarn?.tilSøknadsfelt(),
                harDereSkriftligAvtaleOmSamvær = forelder.harDereSkriftligSamværsavtale?.tilSøknadsfelt(),
                samværsavtale =
                    dokumentfelt(
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
                skalBarnetBoHosSøkerMenAnnenForelderSamarbeiderIkke =
                    dokumentfelt(
                        BARN_BOR_HOS_SØKER,
                        dokumentMap,
                    ),
            ),
        )
}

private fun no.nav.familie.kontrakter.ef.søknad.Barn.hentPdlAlder(): String =
    Period
        .between(
            this.fødselTermindato?.verdi,
            LocalDate.now(),
        ).years
        .toString()
