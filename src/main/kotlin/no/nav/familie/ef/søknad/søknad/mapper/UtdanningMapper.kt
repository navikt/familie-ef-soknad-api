package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.DatoFelt
import no.nav.familie.ef.søknad.søknad.domain.PeriodeFelt
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.søknad.domain.TidligereUtdanning
import no.nav.familie.ef.søknad.søknad.domain.UnderUtdanning
import no.nav.familie.ef.søknad.utils.Språktekster.NårSkalDuVæreElevStudent
import no.nav.familie.ef.søknad.utils.Språktekster.NårVarDuElevStudent
import no.nav.familie.ef.søknad.utils.Språktekster.Utdanning
import no.nav.familie.ef.søknad.utils.Språktekster.UtdanningenDuSkalTa
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.tilBooleanFelt
import no.nav.familie.ef.søknad.utils.tilDesimaltall
import no.nav.familie.ef.søknad.utils.tilHeltall
import no.nav.familie.ef.søknad.utils.tilLocalDate
import no.nav.familie.ef.søknad.utils.tilNullableTekstFelt
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.ef.søknad.utils.tilTekstFelt
import no.nav.familie.kontrakter.ef.søknad.Datoperiode
import no.nav.familie.kontrakter.ef.søknad.GjeldendeUtdanning
import no.nav.familie.kontrakter.ef.søknad.MånedÅrPeriode
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate
import java.time.YearMonth
import no.nav.familie.ef.søknad.utils.Språktekster.TidligereUtdanning as SpråkTeksterTidligereUtdanning
import no.nav.familie.kontrakter.ef.søknad.TidligereUtdanning as TidligereUtdanningKontrakt
import no.nav.familie.kontrakter.ef.søknad.UnderUtdanning as UnderUtdanningKontrakt

object UtdanningMapper : Mapper<UnderUtdanning, UnderUtdanningKontrakt>(UtdanningenDuSkalTa) {
    override fun mapDto(data: UnderUtdanning): UnderUtdanningKontrakt =
        UnderUtdanningKontrakt(
            skoleUtdanningssted = data.skoleUtdanningssted.tilSøknadsfelt(),
            utdanning = null,
            gjeldendeUtdanning =
                Søknadsfelt(
                    Utdanning.hentTekst(),
                    GjeldendeUtdanning(
                        data.linjeKursGrad.tilSøknadsfelt(),
                        Søknadsfelt(
                            NårSkalDuVæreElevStudent.hentTekst(),
                            Datoperiode(
                                data.periode.fra.tilLocalDate(),
                                data.periode.til.tilLocalDate(),
                            ),
                        ),
                    ),
                ),
            offentligEllerPrivat = data.offentligEllerPrivat.tilSøknadsfelt(),
            hvorMyeSkalDuStudere = data.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall),
            heltidEllerDeltid = data.heltidEllerDeltid.tilSøknadsfelt(),
            hvaErMåletMedUtdanningen = data.målMedUtdanning?.tilSøknadsfelt(),
            utdanningEtterGrunnskolen = data.harTattUtdanningEtterGrunnskolen.tilSøknadsfelt(),
            tidligereUtdanninger = data.tidligereUtdanning?.let { mapTidligereUtdanning(it) },
            semesteravgift = mapUtgifterTilUtdanning(data.semesteravgift),
            studieavgift = mapUtgifterTilUtdanning(data.studieavgift),
            eksamensgebyr = mapUtgifterTilUtdanning(data.eksamensgebyr),
        )

    fun mapTilDto(underUtdanningKontrakt: UnderUtdanningKontrakt?): UnderUtdanning? {
        if (underUtdanningKontrakt != null) {
            return underUtdanningKontrakt.gjeldendeUtdanning?.let {
                // For å unngå "Smart cast to is a public API property declared in different module"
                UnderUtdanning(
                    skoleUtdanningssted = underUtdanningKontrakt.skoleUtdanningssted.tilTekstFelt(),
                    arbeidsmengde = underUtdanningKontrakt.hvorMyeSkalDuStudere.tilNullableTekstFelt(),
                    harTattUtdanningEtterGrunnskolen = underUtdanningKontrakt.utdanningEtterGrunnskolen.tilBooleanFelt(),
                    heltidEllerDeltid = underUtdanningKontrakt.heltidEllerDeltid.tilTekstFelt(),
                    offentligEllerPrivat = underUtdanningKontrakt.offentligEllerPrivat.tilTekstFelt(),
                    målMedUtdanning = underUtdanningKontrakt.hvaErMåletMedUtdanningen.tilNullableTekstFelt(),
                    linjeKursGrad = it.verdi.linjeKursGrad.tilTekstFelt(),
                    periode = it.verdi.nårVarSkalDuVæreElevStudent.tilPeriodeFelt(),
                    semesteravgift = underUtdanningKontrakt.semesteravgift.tilNullableTekstFelt(),
                    studieavgift = underUtdanningKontrakt.studieavgift.tilNullableTekstFelt(),
                    eksamensgebyr = underUtdanningKontrakt.eksamensgebyr.tilNullableTekstFelt(),
                    tidligereUtdanning = mapTilTidligereUtdanningDto(underUtdanningKontrakt.tidligereUtdanninger?.verdi),
                )
            }
        }
        return null
    }

    private fun mapTidligereUtdanning(tidligereUtdanning: List<TidligereUtdanning>): Søknadsfelt<List<TidligereUtdanningKontrakt>> {
        val tidligereUtdanningList =
            tidligereUtdanning.map {
                TidligereUtdanningKontrakt(
                    it.linjeKursGrad.tilSøknadsfelt(),
                    Søknadsfelt(
                        NårVarDuElevStudent.hentTekst(),
                        MånedÅrPeriode(
                            it.periode.fra
                                .tilLocalDate()
                                .month,
                            it.periode.fra
                                .tilLocalDate()
                                .year,
                            it.periode.til
                                .tilLocalDate()
                                .month,
                            it.periode.til
                                .tilLocalDate()
                                .year,
                        ),
                    ),
                )
            }
        return Søknadsfelt(SpråkTeksterTidligereUtdanning.hentTekst(), tidligereUtdanningList)
    }

    private fun mapTilTidligereUtdanningDto(tidligereUtdanningKontrakt: List<TidligereUtdanningKontrakt>?): List<TidligereUtdanning> =
        tidligereUtdanningKontrakt?.map {
            TidligereUtdanning(
                it.linjeKursGrad.tilTekstFelt(),
                it.nårVarSkalDuVæreElevStudent.månedÅrSøknadsfeltTilPeriodeFelt(),
            )
        } ?: emptyList()

    private fun Søknadsfelt<MånedÅrPeriode>.månedÅrSøknadsfeltTilPeriodeFelt() =
        PeriodeFelt(
            label = label,
            fra = DatoFelt(label, LocalDate.of(verdi.fraÅr, verdi.fraMåned, 1).toString()),
            til = DatoFelt(label, YearMonth.of(verdi.tilÅr, verdi.tilMåned).atEndOfMonth().toString()),
        )

    private fun Søknadsfelt<Datoperiode>.tilPeriodeFelt() =
        PeriodeFelt(
            label = label,
            fra = DatoFelt(label, verdi.fra.toString()),
            til = DatoFelt(label, verdi.til.toString()),
        )

    private fun mapUtgifterTilUtdanning(utgift: TekstFelt?): Søknadsfelt<Double>? {
        return utgift?.let {
            return if (it.verdi.isNotBlank()) {
                utgift.tilSøknadsfelt(String::tilDesimaltall)
            } else {
                null
            }
        }
    }
}
