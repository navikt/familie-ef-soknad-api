package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.minside.dto.MineStønaderDto
import no.nav.familie.ef.søknad.minside.dto.PeriodeStatus
import no.nav.familie.ef.søknad.minside.dto.PeriodeStatus.FREMTIDIG_UTEN_OPPHOLD
import no.nav.familie.ef.søknad.minside.dto.PeriodeStatus.INGEN
import no.nav.familie.ef.søknad.minside.dto.PeriodeStatus.LØPENDE_UTEN_OPPHOLD
import no.nav.familie.ef.søknad.minside.dto.PeriodeStatus.TIDLIGERE_ELLER_OPPHOLD
import no.nav.familie.ef.søknad.minside.dto.Stønad
import no.nav.familie.ef.søknad.minside.dto.StønadsperiodeDto
import no.nav.familie.ef.søknad.utils.DatoUtil.dagensDato
import no.nav.familie.kontrakter.felles.Datoperiode
import no.nav.familie.kontrakter.felles.erSammenhengende
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SaksbehandlingService(private val saksbehandlingClient: SaksbehandlingClient) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun hentStønadsperioderForBruker(): MineStønaderDto {
        val stønadsperioder = saksbehandlingClient.hentStønadsperioderForBruker()

        val mineStønaderDto = MineStønaderDto(
            overgangsstønad = utledStønad(stønadsperioder.overgangsstønad),
            barnetilsyn = utledStønad(stønadsperioder.barnetilsyn),
            skolepenger = utledStønad(stønadsperioder.skolepenger),
        )

        return mineStønaderDto
    }

    private fun utledStønad(stønader: List<StønadsperiodeDto>): Stønad {
        val perioderSortertPåDato = stønader.sortedBy { it.fraDato }

        val (periodeStatus, relevantePerioder) = utledPeriodeStatusMedPerioder(perioderSortertPåDato)
        val startDato = utledStartDato(periodeStatus, relevantePerioder)
        val sluttDato = utledTilDato(periodeStatus, relevantePerioder)

        return Stønad(
            periodeStatus = periodeStatus,
            startDato = startDato,
            sluttDato = sluttDato,
            perioder = perioderSortertPåDato,
        )
    }

    private fun utledTilDato(
        periodeStatus: PeriodeStatus,
        relevantePerioder: List<StønadsperiodeDto>,
    ) =
        if (periodeStatus === FREMTIDIG_UTEN_OPPHOLD || periodeStatus === LØPENDE_UTEN_OPPHOLD) relevantePerioder.last().tilDato else null

    private fun utledStartDato(
        periodeStatus: PeriodeStatus,
        relevantePerioder: List<StønadsperiodeDto>,
    ) = if (periodeStatus === FREMTIDIG_UTEN_OPPHOLD) relevantePerioder.first().fraDato else null

    private fun utledPeriodeStatusMedPerioder(perioder: List<StønadsperiodeDto>): Pair<PeriodeStatus, List<StønadsperiodeDto>> {
        if (perioder.isEmpty()) {
            return Pair(INGEN, emptyList())
        }

        val harPerioderSiste6mnd =
            perioder.filter { it.tilDato < dagensDato() && it.tilDato > dagensDato().minusMonths(6) }.isNotEmpty()
        val perioderMedFremtidigSluttdato = perioder.filter { it.tilDato >= dagensDato() }
        val periodeStatus = utledPeriodeStatus(perioderMedFremtidigSluttdato, harPerioderSiste6mnd)

        return when (periodeStatus) {
            INGEN, TIDLIGERE_ELLER_OPPHOLD -> Pair(periodeStatus, emptyList())
            LØPENDE_UTEN_OPPHOLD, FREMTIDIG_UTEN_OPPHOLD -> Pair(periodeStatus, perioderMedFremtidigSluttdato)
        }
    }

    private fun utledPeriodeStatus(
        stønadsperioderMedFremtidigSluttDato: List<StønadsperiodeDto>,
        harPerioderSiste6mnd: Boolean,
    ): PeriodeStatus {
        val datoPerioderMedFremtidigSluttdato =
            stønadsperioderMedFremtidigSluttDato.map { Datoperiode(fom = it.fraDato, tom = it.tilDato) }
        val harFremtidigePerioderOgErSammenhengende =
            datoPerioderMedFremtidigSluttdato.erSammenhengende() && datoPerioderMedFremtidigSluttdato.isNotEmpty()
        val inneholderDagensDato = datoPerioderMedFremtidigSluttdato.any { it.inneholder(dagensDato()) }
        return if (harFremtidigePerioderOgErSammenhengende && inneholderDagensDato) {
            LØPENDE_UTEN_OPPHOLD
        } else if (harFremtidigePerioderOgErSammenhengende && !harPerioderSiste6mnd) {
            FREMTIDIG_UTEN_OPPHOLD
        } else {
            TIDLIGERE_ELLER_OPPHOLD
        }
    }
}
