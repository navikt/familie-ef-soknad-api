package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.Barn
import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynGjenbrukDto
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.util.FnrGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import kotlin.test.assertNotNull

internal class SøknadBarnetilsynMapperTest {
    private val mapper = SøknadBarnetilsynMapper()

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    private val søknad: SøknadBarnetilsynDto =
        objectMapper.readValue(
            File("src/test/resources/barnetilsyn/Barnetilsynsøknad.json"),
            SøknadBarnetilsynDto::class.java,
        )

    val søknadBTDto =
        objectMapper.readValue(
            File("src/test/resources/barnetilsyn/BarnetilsynsøknadBarnMedFeilAlderIFnr.json"),
            SøknadBarnetilsynDto::class.java,
        )

    @Test
    fun `Barnetilsyn skal mappes `() {
        val mapped = mapper.mapTilIntern(søknad, innsendingMottatt)

        assertNotNull(
            mapped.søknad.barn.verdi
                .first()
                .navn
                ?.verdi,
        )
    }

    @Test
    fun `Folkeregistrert barn har riktig alder`() {
        val fødseldato1 = LocalDate.now().minusYears(7)
        val fødseldato2 = LocalDate.now().minusYears(8)
        val søknadBT = mapper.mapTilIntern(søknadBTDto, innsendingMottatt).søknad
        val barn1 = lagBarnMedFødselsdato(søknadBT, fødseldato1)
        val barn2 = lagBarnMedFødselsdato(søknadBT, fødseldato2)
        val søknadBarnetilsyn = søknadBT.copy(barn = Søknadsfelt("BarnaDine", listOf(barn1, barn2)))

        val søknadBarnetilsynGjenbrukDto = mapper.mapTilDto(søknadBarnetilsyn)

        assertThatBarnHarRiktigAlder(søknadBarnetilsynGjenbrukDto, barn1, fødseldato1)
        assertThatBarnHarRiktigAlder(søknadBarnetilsynGjenbrukDto, barn2, fødseldato1)
    }

    private fun lagBarnMedFødselsdato(
        søknadBT: SøknadBarnetilsyn,
        fødseldato1: LocalDate,
    ): no.nav.familie.kontrakter.ef.søknad.Barn {
        val barnSøknadsfelt1 =
            søknadBT.barn.verdi
                .first()
                .fødselTermindato
                ?.copy(verdi = fødseldato1)

        val barn1 =
            søknadBT.barn.verdi
                .first()
                .copy(fødselTermindato = barnSøknadsfelt1)
        return barn1
    }

    private fun assertThatBarnHarRiktigAlder(
        søknadBarnetilsynGjenbrukDto: SøknadBarnetilsynGjenbrukDto?,
        barn1: no.nav.familie.kontrakter.ef.søknad.Barn,
        fødseldato1: LocalDate,
    ) {
        assertThat(
            alderBarn(søknadBarnetilsynGjenbrukDto, plukk = { it.find { barn -> erSammeBarn(barn, barn1) }!! }),
        ).isEqualTo(fødseldato1.årTilNå())
    }

    private fun erSammeBarn(
        barn: Barn,
        barn1: no.nav.familie.kontrakter.ef.søknad.Barn,
    ) = barn.ident!!.verdi == barn1.fødselsnummer!!.verdi.verdi

    private fun alderBarn(
        søknadBarnetilsynGjenbrukDto: SøknadBarnetilsynGjenbrukDto?,
        plukk: (List<Barn>) -> Barn,
    ): Int =
        søknadBarnetilsynGjenbrukDto
            ?.person
            ?.barn
            ?.let { plukk(it) }
            ?.alder
            ?.verdi
            ?.toInt()!!

    @Test
    internal fun `skal kun ta med barn som skal ha barnepass`() {
        val identForBarnMedBarnepass = FnrGenerator.generer(LocalDate.now())
        val person = søknad.person
        val barn = person.barn

        fun lagBarn(
            ident: String,
            skalHaBarnepass: Boolean?,
        ) = barn[0]
            .copy(id = ident, ident = TekstFelt("", ident), skalHaBarnepass = skalHaBarnepass?.let { BooleanFelt("", it) })

        val mapped =
            mapper.mapTilIntern(
                søknad.copy(
                    person =
                        person.copy(
                            barn =
                                listOf(
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

private fun LocalDate.årTilNå(): Int = Period.between(this, LocalDate.now()).years
