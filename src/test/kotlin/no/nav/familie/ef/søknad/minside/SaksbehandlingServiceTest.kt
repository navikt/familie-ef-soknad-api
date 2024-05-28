package no.nav.familie.ef.søknad.minside

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import no.nav.familie.ef.søknad.minside.dto.PeriodeStatus
import no.nav.familie.ef.søknad.minside.dto.Stønad
import no.nav.familie.ef.søknad.minside.dto.StønadsperiodeDto
import no.nav.familie.ef.søknad.minside.dto.StønadsperioderDto
import no.nav.familie.ef.søknad.utils.DatoUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SaksbehandlingServiceTest {
    private val saksbehandlingClient = mockk<SaksbehandlingClient>()
    private val saksbehandlingService = SaksbehandlingService(saksbehandlingClient)

    // Ønsker ikke å bruke LocalDate.now() i tester -> tar utgangspunkt i 12. feb 2024 som dagens dato
    private val dagensDato = LocalDate.of(2024, 2, 12)

    @BeforeEach
    fun setUp() {
        mockkObject(DatoUtil)
        every { DatoUtil.dagensDato() } returns dagensDato
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(DatoUtil)
    }

    @Test
    internal fun `ingen stønadsperioder skal returnere periodestatus INGEN og tomme perioder`() {
        val stønadsperioderDto = StønadsperioderDto(emptyList(), emptyList(), emptyList())

        every { saksbehandlingClient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        val stønadsperioderForBruker = saksbehandlingService.hentStønadsperioderForBruker()

        assertTommePerioder(stønadsperioderForBruker.overgangsstønad)
        assertTommePerioder(stønadsperioderForBruker.barnetilsyn)
        assertTommePerioder(stønadsperioderForBruker.skolepenger)
    }

    @Test
    internal fun `perioder tidligere, nå og fremover uten opphold skal mappes til LØPENDE_UTEN_OPPHOLD`() {
        val sluttDatoRelevantePerioder = LocalDate.of(2025, 9, 30)
        val periode1 = StønadsperiodeDto(LocalDate.of(2023, 11, 1), LocalDate.of(2023, 11, 30), 13665, 0, 4000)
        val periode2 = StønadsperiodeDto(LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 31), 4215, 400000, 0)
        val periode3 = StønadsperiodeDto(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 9, 30), 19065, 0, 0)

        val stønadsperioder = listOf(periode1, periode2, periode3)
        val stønadsperioderDto = StønadsperioderDto(stønadsperioder, emptyList(), emptyList())

        every { saksbehandlingClient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        val stønadsperioderForBruker = saksbehandlingService.hentStønadsperioderForBruker()

        assertThat(stønadsperioderForBruker.overgangsstønad.periodeStatus).isEqualTo(PeriodeStatus.LØPENDE_UTEN_OPPHOLD)
        assertThat(stønadsperioderForBruker.overgangsstønad.startDato).isNull()
        assertThat(stønadsperioderForBruker.overgangsstønad.sluttDato).isEqualTo(sluttDatoRelevantePerioder)
        assertThat(stønadsperioderForBruker.overgangsstønad.perioder).isEqualTo(stønadsperioder)
        assertTommePerioder(stønadsperioderForBruker.barnetilsyn)
        assertTommePerioder(stønadsperioderForBruker.skolepenger)
    }

    @Test
    internal fun `perioder fremover uten opphold skal mappes til FREMTIDIG_UTEN_OPPHOLD`() {
        val startDatoRelevantePerioder = LocalDate.of(2024, 3, 1)
        val sluttDatoRelevantePerioder = LocalDate.of(2027, 1, 31)
        val fremtidigPeriode = StønadsperiodeDto(startDatoRelevantePerioder, sluttDatoRelevantePerioder, 19065, 0, 0)

        val stønadsperioder = listOf(fremtidigPeriode)
        val stønadsperioderDto = StønadsperioderDto(emptyList(), stønadsperioder, emptyList())

        every { saksbehandlingClient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        val stønadsperioderForBruker = saksbehandlingService.hentStønadsperioderForBruker()

        assertThat(stønadsperioderForBruker.barnetilsyn.periodeStatus).isEqualTo(PeriodeStatus.FREMTIDIG_UTEN_OPPHOLD)
        assertThat(stønadsperioderForBruker.barnetilsyn.startDato).isEqualTo(startDatoRelevantePerioder)
        assertThat(stønadsperioderForBruker.barnetilsyn.sluttDato).isEqualTo(sluttDatoRelevantePerioder)
        assertThat(stønadsperioderForBruker.barnetilsyn.perioder).isEqualTo(stønadsperioder)
        assertTommePerioder(stønadsperioderForBruker.overgangsstønad)
        assertTommePerioder(stønadsperioderForBruker.skolepenger)
    }

    @Test
    internal fun `perioder fremover med opphold skal mappes til TIDLIGERE_ELLER_OPPHOLD`() {
        val fremtidigPeriode1 = StønadsperiodeDto(LocalDate.of(2024, 4, 1), LocalDate.of(2025, 3, 31), 19065, 0, 0)
        val fremtidigPeriode2 = StønadsperiodeDto(LocalDate.of(2025, 5, 31), LocalDate.of(2026, 11, 30), 19065, 0, 0)

        val stønadsperioder = listOf(fremtidigPeriode1, fremtidigPeriode2)
        val stønadsperioderDto = StønadsperioderDto(emptyList(), emptyList(), stønadsperioder)

        every { saksbehandlingClient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        val stønadsperioderForBruker = saksbehandlingService.hentStønadsperioderForBruker()

        assertThat(stønadsperioderForBruker.skolepenger.periodeStatus).isEqualTo(PeriodeStatus.TIDLIGERE_ELLER_OPPHOLD)
        assertThat(stønadsperioderForBruker.skolepenger.startDato).isNull()
        assertThat(stønadsperioderForBruker.skolepenger.sluttDato).isNull()
        assertThat(stønadsperioderForBruker.skolepenger.perioder).isEqualTo(stønadsperioder)
        assertTommePerioder(stønadsperioderForBruker.overgangsstønad)
        assertTommePerioder(stønadsperioderForBruker.barnetilsyn)
    }

    @Test
    internal fun `perioder tidligere som er eldre enn 6mnd, ingen perioder nå men perioder fremover uten opphold skal mappes til FREMTIDIG_UTEN_OPPHOLD`() {
        val startDatoRelevantePerioder = LocalDate.of(2024, 3, 1)
        val sluttDatoRelevantePerioder = LocalDate.of(2025, 1, 31)
        val tidligerePeriode = StønadsperiodeDto(LocalDate.of(2023, 3, 1), LocalDate.of(2023, 7, 31), 12000, 0, 0)
        val fremtidigPeriode = StønadsperiodeDto(startDatoRelevantePerioder, sluttDatoRelevantePerioder, 19065, 0, 0)

        val stønadsperioder = listOf(tidligerePeriode, fremtidigPeriode)
        val stønadsperioderDto = StønadsperioderDto(emptyList(), stønadsperioder, emptyList())

        every { saksbehandlingClient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        val stønadsperioderForBruker = saksbehandlingService.hentStønadsperioderForBruker()

        assertThat(stønadsperioderForBruker.barnetilsyn.periodeStatus).isEqualTo(PeriodeStatus.FREMTIDIG_UTEN_OPPHOLD)
        assertThat(stønadsperioderForBruker.barnetilsyn.startDato).isEqualTo(startDatoRelevantePerioder)
        assertThat(stønadsperioderForBruker.barnetilsyn.sluttDato).isEqualTo(sluttDatoRelevantePerioder)
        assertThat(stønadsperioderForBruker.barnetilsyn.perioder).isEqualTo(stønadsperioder)
        assertTommePerioder(stønadsperioderForBruker.overgangsstønad)
        assertTommePerioder(stønadsperioderForBruker.skolepenger)
    }

    @Test
    internal fun `perioder tidligere innen de siste 6mnd, ingen perioder nå men perioder fremover uten opphold skal mappes til TIDLIGERE_ELLER_OPPHOLD`() {
        val startDatoRelevantePerioder = LocalDate.of(2024, 3, 1)
        val sluttDatoRelevantePerioder = LocalDate.of(2025, 1, 31)
        val tidligerePeriode = StønadsperiodeDto(LocalDate.of(2023, 3, 1), LocalDate.of(2024, 1, 31), 12000, 0, 0)
        val fremtidigPeriode = StønadsperiodeDto(startDatoRelevantePerioder, sluttDatoRelevantePerioder, 19065, 0, 0)

        val stønadsperioder = listOf(tidligerePeriode, fremtidigPeriode)
        val stønadsperioderDto = StønadsperioderDto(emptyList(), stønadsperioder, emptyList())

        every { saksbehandlingClient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        val stønadsperioderForBruker = saksbehandlingService.hentStønadsperioderForBruker()

        assertThat(stønadsperioderForBruker.barnetilsyn.periodeStatus).isEqualTo(PeriodeStatus.TIDLIGERE_ELLER_OPPHOLD)
        assertThat(stønadsperioderForBruker.barnetilsyn.startDato).isNull()
        assertThat(stønadsperioderForBruker.barnetilsyn.sluttDato).isNull()
        assertThat(stønadsperioderForBruker.barnetilsyn.perioder).isEqualTo(stønadsperioder)
        assertTommePerioder(stønadsperioderForBruker.overgangsstønad)
        assertTommePerioder(stønadsperioderForBruker.skolepenger)
    }

    @Test
    internal fun `tidligere perioder skal mappes til TIDLIGERE_ELLER_OPPHOLD`() {
        val periode1 = StønadsperiodeDto(LocalDate.of(2021, 11, 1), LocalDate.of(2020, 11, 30), 13665, 0, 0)
        val periode2 = StønadsperiodeDto(LocalDate.of(2021, 12, 1), LocalDate.of(2020, 12, 31), 4215, 0, 0)
        val periode3 = StønadsperiodeDto(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 9, 30), 19065, 0, 0)

        val stønadsperioder = listOf(periode1, periode2, periode3)
        val stønadsperioderDto = StønadsperioderDto(emptyList(), emptyList(), stønadsperioder)

        every { saksbehandlingClient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        val stønadsperioderForBruker = saksbehandlingService.hentStønadsperioderForBruker()

        assertThat(stønadsperioderForBruker.skolepenger.periodeStatus).isEqualTo(PeriodeStatus.TIDLIGERE_ELLER_OPPHOLD)
        assertThat(stønadsperioderForBruker.skolepenger.startDato).isNull()
        assertThat(stønadsperioderForBruker.skolepenger.sluttDato).isNull()
        assertThat(stønadsperioderForBruker.skolepenger.perioder).isEqualTo(stønadsperioder)
        assertTommePerioder(stønadsperioderForBruker.overgangsstønad)
        assertTommePerioder(stønadsperioderForBruker.barnetilsyn)
    }

    @Test
    internal fun `tidligere perioder med opphold skal mappes til TIDLIGERE_ELLER_OPPHOLD`() {
        val periode1 = StønadsperiodeDto(LocalDate.of(2021, 11, 1), LocalDate.of(2020, 11, 30), 13665, 0, 0)
        val periode3 = StønadsperiodeDto(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 9, 30), 19065, 0, 0)

        val stønadsperioder = listOf(periode1, periode3)
        val stønadsperioderDto = StønadsperioderDto(emptyList(), emptyList(), stønadsperioder)

        every { saksbehandlingClient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        val stønadsperioderForBruker = saksbehandlingService.hentStønadsperioderForBruker()

        assertThat(stønadsperioderForBruker.skolepenger.periodeStatus).isEqualTo(PeriodeStatus.TIDLIGERE_ELLER_OPPHOLD)
        assertThat(stønadsperioderForBruker.skolepenger.startDato).isNull()
        assertThat(stønadsperioderForBruker.skolepenger.sluttDato).isNull()
        assertThat(stønadsperioderForBruker.skolepenger.perioder).isEqualTo(stønadsperioder)
        assertTommePerioder(stønadsperioderForBruker.overgangsstønad)
        assertTommePerioder(stønadsperioderForBruker.barnetilsyn)
    }

    @Test
    internal fun `perioder tidligere, nå og fremover med opphold skal mappes til TIDLIGERE_ELLER_OPPHOLD`() {
        val periode1 = StønadsperiodeDto(LocalDate.of(2023, 11, 1), LocalDate.of(2024, 2, 29), 4215, 0, 0)
        val periode2 = StønadsperiodeDto(LocalDate.of(2024, 4, 1), LocalDate.of(2025, 9, 30), 19065, 0, 0)

        val stønadsperioder = listOf(periode1, periode2)
        val stønadsperioderDto = StønadsperioderDto(stønadsperioder, emptyList(), emptyList())

        every { saksbehandlingClient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        val stønadsperioderForBruker = saksbehandlingService.hentStønadsperioderForBruker()

        assertThat(stønadsperioderForBruker.overgangsstønad.periodeStatus).isEqualTo(PeriodeStatus.TIDLIGERE_ELLER_OPPHOLD)
        assertThat(stønadsperioderForBruker.overgangsstønad.startDato).isNull()
        assertThat(stønadsperioderForBruker.overgangsstønad.sluttDato).isNull()
        assertThat(stønadsperioderForBruker.overgangsstønad.perioder).isEqualTo(stønadsperioder)
        assertTommePerioder(stønadsperioderForBruker.barnetilsyn)
        assertTommePerioder(stønadsperioderForBruker.skolepenger)
    }

    private fun assertTommePerioder(stønad: Stønad) {
        assertThat(stønad.periodeStatus).isEqualTo(PeriodeStatus.INGEN)
        assertThat(stønad.startDato).isNull()
        assertThat(stønad.sluttDato).isNull()
        assertThat(stønad.perioder).isEmpty()
    }
}
