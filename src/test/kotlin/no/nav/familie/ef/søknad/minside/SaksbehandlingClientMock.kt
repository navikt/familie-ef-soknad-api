package no.nav.familie.ef.søknad.minside

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.minside.dto.StønadsperioderDto
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

        val stønadsperioderDto = StønadsperioderDto(
            overgangsstønad = stønadsperioderOvergangsstønad,
            barnetilsyn = stønadsperioderBarnetilsyn,
            skolepenger = stønadsperioderSkolepenger
        )

        every { saksbehandlingCLient.hentStønadsperioderForBruker() } returns stønadsperioderDto

        return saksbehandlingCLient
    }

    private val stønadsperioderOvergangsstønad: List<StønadsperiodeDto> = listOf(
        StønadsperiodeDto(
            fraDato = LocalDate.of(2024, 1, 1),
            tilDato = LocalDate.of(2025, 9, 30),
            beløp = 19065
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2023, 12, 1),
            tilDato = LocalDate.of(2022, 12, 31),
            beløp = 4215
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2023, 11, 1),
            tilDato = LocalDate.of(2023, 11, 30),
            beløp = 13665
        ),
    )

    private val stønadsperioderBarnetilsyn: List<StønadsperiodeDto> = listOf(
        StønadsperiodeDto(
            fraDato = LocalDate.of(2022, 1, 1),
            tilDato = LocalDate.of(2023, 9, 30),
            beløp = 19065
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2021, 12, 1),
            tilDato = LocalDate.of(2021, 12, 31),
            beløp = 4215
        ),
        StønadsperiodeDto(
            fraDato = LocalDate.of(2021, 11, 1),
            tilDato = LocalDate.of(2021, 11, 30),
            beløp = 13665
        ),
    )

    private val stønadsperioderSkolepenger: List<StønadsperiodeDto> = listOf(
        StønadsperiodeDto(
            fraDato = LocalDate.of(2024, 3, 1),
            tilDato = LocalDate.of(2027, 1, 31),
            beløp = 19065
        ),
    )
}
