package no.nav.familie.ef.søknad.minside

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.minside.dto.MineStønaderDto
import no.nav.familie.ef.søknad.minside.dto.StønadsperiodeDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDate

@Configuration
@Profile("mock-saksbehandling")
class SaksbehandlingClientMock {

    @Bean
    @Primary
    fun saksbehandlingClient(): SaksbehandlingClient {
        val saksbehandlingCLient: SaksbehandlingClient = mockk()

        val mineStønaderDto = MineStønaderDto(
            overgangsstønad = stønadsperioderOvergangsstønad,
            barnetilsyn = stønadsperioderBarnetilsyn,
            skolepenger = stønadsperioderSkolepenger
        )

        every { saksbehandlingCLient.hentStønadsperioderForBruker() } returns mineStønaderDto

        return saksbehandlingCLient
    }

    private val stønadsperioderOvergangsstønad: List<StønadsperiodeDto> = listOf(
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 1, 1),
            tilDato = LocalDate.of(2022, 7, 31),
            beløp = 653
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 8, 1),
            tilDato = LocalDate.of(2022, 10, 31),
            beløp = 2542
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 11, 1),
            tilDato = LocalDate.of(2023, 5, 31),
            beløp = 11200
        ),
    )

    private val stønadsperioderBarnetilsyn: List<StønadsperiodeDto> = listOf(
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 1, 1),
            tilDato = LocalDate.of(2022, 7, 31),
            beløp = 653
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 8, 1),
            tilDato = LocalDate.of(2022, 10, 31),
            beløp = 2542
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 11, 1),
            tilDato = LocalDate.of(2023, 5, 31),
            beløp = 11200
        ),
    )

    private val stønadsperioderSkolepenger: List<StønadsperiodeDto> = listOf(
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 1, 1),
            tilDato = LocalDate.of(2022, 7, 31),
            beløp = 653
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 8, 1),
            tilDato = LocalDate.of(2022, 10, 31),
            beløp = 2542
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 11, 1),
            tilDato = LocalDate.of(2023, 5, 31),
            beløp = 11200
        ),
    )
}
