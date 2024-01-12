package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.mapper.SøknadBarnetilsynMapper
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.util.FnrGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertNotNull

internal class SøknadBarnetilsynMapperTest {

    private val mapper = SøknadBarnetilsynMapper()

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    private val søknad: SøknadBarnetilsynDto = objectMapper.readValue(
        File("src/test/resources/barnetilsyn/Barnetilsynsøknad.json"),
        SøknadBarnetilsynDto::class.java,
    )

    @Test
    fun `Barnetilsyn skal mappes `() {
        val mapped = mapper.mapTilIntern(søknad, innsendingMottatt)

        assertNotNull(mapped.søknad.barn.verdi.first().navn?.verdi)
    }

    @Test
    internal fun `skal kun ta med barn som skal ha barnepass`() {
        val identForBarnMedBarnepass = FnrGenerator.generer(LocalDate.now())
        val person = søknad.person
        val barn = person.barn
        fun lagBarn(ident: String, skalHaBarnepass: Boolean?) = barn[0]
            .copy(id = ident, ident = TekstFelt("", ident), skalHaBarnepass = skalHaBarnepass?.let { BooleanFelt("", it) })

        val mapped = mapper.mapTilIntern(
            søknad.copy(
                person = person.copy(
                    barn = listOf(
                        lagBarn(FnrGenerator.generer(LocalDate.now().minusDays(1)), false),
                        lagBarn(identForBarnMedBarnepass, true),
                        lagBarn(FnrGenerator.generer(LocalDate.now().plusDays(1)), null),
                    ),
                ),
            ),
            innsendingMottatt,
        )
        val mappedBarn = mapped.søknad.barn.verdi
        assertThat(mappedBarn).hasSize(1)
        assertThat(mappedBarn[0].fødselsnummer?.verdi?.verdi).isEqualTo(identForBarnMedBarnepass)
    }
}
