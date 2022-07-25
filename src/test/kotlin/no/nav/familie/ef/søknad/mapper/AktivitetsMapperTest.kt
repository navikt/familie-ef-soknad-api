package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.AktivitetsMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.kontrakter.ef.søknad.Aksjeselskap
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

internal class AktivitetsMapperTest {

    // Gitt
    private val aktivitet = søknadDto().aktivitet

    // Når
    private val aktivitetMapped = AktivitetsMapper.map(aktivitet, mapOf()).verdi

    @Test
    fun `Skal kaste feil ved blank verdi på arbeidsmengde `() {
        val arbeidsmengdeUtenVerdi = aktivitet.arbeidsforhold?.first()?.arbeidsmengde?.copy(verdi = "")
        val arbeidsforhold = aktivitet.arbeidsforhold?.first()?.copy(arbeidsmengde = arbeidsmengdeUtenVerdi)
        assertThrows<NumberFormatException> {
            AktivitetsMapper.mapArbeidsforhold(listOf(arbeidsforhold!!))
        }
    }

    @Test
    fun `Map hvordanErArbeidssituasjonen label `() {
        Assertions.assertThat(aktivitetMapped.hvordanErArbeidssituasjonen.label).isEqualTo("Hva er din arbeidsituasjon?")
    }

    @Test
    fun `Map hvordanErArbeidssituasjonen verdi `() {
        Assertions.assertThat(aktivitetMapped.hvordanErArbeidssituasjonen.verdi).hasSize(14)
    }

    @Test
    fun `Map arbeidsforhold verdi `() {
        Assertions.assertThat(aktivitetMapped.arbeidsforhold).isNotNull
    }

    private val firma = aktivitetMapped.firmaer?.verdi?.first()

    @Test
    fun `Map firmanavn label `() {
        Assertions.assertThat(firma?.firmanavn?.label).isEqualTo("Navn på arbeidsgiver")
    }

    @Test
    fun `Map firmanavn verdi `() {
        Assertions.assertThat(firma?.firmanavn?.verdi).isEqualTo("Boller og brus")
    }

    @Test
    fun `Map arbeidsmengde label `() {
        Assertions.assertThat(firma?.arbeidsmengde?.label).isEqualTo("Arbeidsmengde")
    }

    @Test
    fun `Map arbeidsmengde verdi `() {
        Assertions.assertThat(firma?.arbeidsmengde?.verdi).isEqualTo(34)
    }

    @Test
    fun `Map etableringsdato label `() {
        Assertions.assertThat(firma?.etableringsdato?.label).isEqualTo("Etableringsdato")
    }

    @Test
    fun `Map etableringsdato verdi `() {
        Assertions.assertThat(firma?.etableringsdato?.verdi).isEqualTo(LocalDate.of(2020, 3, 27))
    }

    @Test
    fun `Map organisasjonsnummer label `() {
        Assertions.assertThat(firma?.organisasjonsnummer?.label).isEqualTo("Organisasjonsnummer")
    }

    @Test
    fun `Map organisasjonsnummer verdi `() {
        Assertions.assertThat(firma?.organisasjonsnummer?.verdi).isEqualTo("123")
    }

    @Test
    fun `Map hvordanSerArbeidsukenUt label `() {
        Assertions.assertThat(firma?.hvordanSerArbeidsukenUt?.label).isEqualTo("Arbeidsukebeskrivelse")
    }

    @Test
    fun `Map hvordanSerArbeidsukenUt verdi `() {
        Assertions.assertThat(firma?.hvordanSerArbeidsukenUt?.verdi).isEqualTo("Jobber mandager")
    }

    @Test
    fun `Map etablererEgenVirksomhet label `() {
        Assertions.assertThat((aktivitetMapped.virksomhet?.verdi?.virksomhetsbeskrivelse?.label))
            .isEqualTo("Hva er din arbeidsituasjon?")
    }

    @Test
    fun `Map etablererEgenVirksomhet verdi `() {
        Assertions.assertThat(aktivitetMapped.virksomhet?.verdi?.virksomhetsbeskrivelse?.verdi)
            .isEqualTo("Dette er en spennende gründerbedrift")
    }

    @Test
    fun `Map ansatt i eget AS `() {
        val aksjeselskap: Aksjeselskap = aktivitetMapped.aksjeselskap?.verdi?.get(0)!!
        Assertions.assertThat(aksjeselskap.navn.label).isEqualTo("Navn på aksjeselskapet ditt")
        Assertions.assertThat(aksjeselskap.navn.verdi).isEqualTo("Mitt eget AS")
        Assertions.assertThat(aksjeselskap.arbeidsmengde!!.label).isEqualTo("Hvor mye jobber du?")
        Assertions.assertThat(aksjeselskap.arbeidsmengde!!.verdi).isEqualTo(55)
    }
}
