package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.minside.dto.MineStønaderDto
import no.nav.familie.ef.søknad.minside.dto.PeriodeStatus
import no.nav.familie.ef.søknad.minside.dto.Stønad
import no.nav.familie.ef.søknad.minside.dto.StønadsperiodeDto
import no.nav.familie.kontrakter.felles.Datoperiode
import no.nav.familie.kontrakter.felles.erSammenhengende
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SaksbehandlingService(private val saksbehandlingClient: SaksbehandlingClient) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun hentStønadsperioderForBruker(dagensDato: LocalDate = LocalDate.now()): MineStønaderDto {
        val stønadsperioder = saksbehandlingClient.hentStønadsperioderForBruker()

        val mineStønaderDto =
            MineStønaderDto(
                overgangsstønad = utledStønad(stønadsperioder.overgangsstønad, dagensDato),
                barnetilsyn = utledStønad(stønadsperioder.barnetilsyn, dagensDato),
                skolepenger = utledStønad(stønadsperioder.skolepenger, dagensDato)
            )

        return mineStønaderDto
    }

    private fun utledStønad(stønader: List<StønadsperiodeDto>, dagensDato: LocalDate): Stønad {
        val perioderSortertPåDato = stønader.sortedBy { it.fraDato }

        val (periodeStatus, relevantePerioder) = utledPeriodeStatus(perioderSortertPåDato, dagensDato)
        val startDato =
            utledStartDato(periodeStatus, relevantePerioder)
        val sluttDato =
            utledTilDato(periodeStatus, relevantePerioder)

        return Stønad(
            periodeStatus = periodeStatus,
            startDato = startDato,
            sluttDato = sluttDato,
            perioder = perioderSortertPåDato
        )
    }

    private fun utledTilDato(
        periodeStatus: PeriodeStatus,
        relevantePerioder: List<StønadsperiodeDto>
    ) =
        if (periodeStatus === PeriodeStatus.FREMTIDIG_UTEN_OPPHOLD || periodeStatus === PeriodeStatus.LØPENDE_UTEN_OPPHOLD) relevantePerioder.last().tilDato else null

    private fun utledStartDato(
        periodeStatus: PeriodeStatus,
        relevantePerioder: List<StønadsperiodeDto>
    ) = if (periodeStatus === PeriodeStatus.FREMTIDIG_UTEN_OPPHOLD) relevantePerioder.first().fraDato else null

    private fun utledPeriodeStatus(
        perioder: List<StønadsperiodeDto>,
        dagensDato: LocalDate,
    ): Pair<PeriodeStatus, List<StønadsperiodeDto>> {
        if (perioder.isEmpty()) {
            return Pair(PeriodeStatus.INGEN, emptyList())
        }

        val perioderMedFremtidigSluttdato =
            perioder.filter { it.tilDato >= dagensDato }
        val datoPerioderMedFremtidigSluttdato =
            perioderMedFremtidigSluttdato.map { Datoperiode(fom = it.fraDato, tom = it.tilDato) }

        val erSammenhengende =
            datoPerioderMedFremtidigSluttdato.erSammenhengende() && datoPerioderMedFremtidigSluttdato.isNotEmpty()

        return if (erSammenhengende) {
            val inneholderDagensDato = datoPerioderMedFremtidigSluttdato.any { it.inneholder(dagensDato) }

            return if (inneholderDagensDato) {
                Pair(PeriodeStatus.LØPENDE_UTEN_OPPHOLD, perioder)
            } else {
                Pair(PeriodeStatus.FREMTIDIG_UTEN_OPPHOLD, perioderMedFremtidigSluttdato)
            }
        } else {
            Pair(PeriodeStatus.TIDLIGERE_ELLER_OPPHOLD, emptyList())
        }
    }
}
