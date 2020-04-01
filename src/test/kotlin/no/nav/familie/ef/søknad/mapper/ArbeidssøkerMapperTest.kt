package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.AktivitetsMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ArbeidssøkerMapperTest {

    // Gitt
    private val søknadDto = søknadDto()

    // Når
    private val aktivitet = AktivitetsMapper.map(søknadDto)
    private val arbeidssøker = aktivitet.arbeidssøker?.verdi

    @Test
    fun `Map registrertSomArbeidssøkerNav label `() {
        Assertions.assertThat((arbeidssøker?.registrertSomArbeidssøkerNav?.label))
                .isEqualTo("Er du registrert som arbeidssøker hos NAV?")
    }

    @Test
    fun `Map registrertSomArbeidssøkerNav verdi `() {
        Assertions.assertThat(arbeidssøker?.registrertSomArbeidssøkerNav?.verdi).isTrue()
    }

    @Test
    fun `Map villigTilÅTaImotTilbudOmArbeid label `() {
        Assertions.assertThat((arbeidssøker?.villigTilÅTaImotTilbudOmArbeid?.label))
                .isEqualTo("Er du villig til å ta imot tilbud om arbeid eller arbeidsmarkedstiltak?")
    }

    @Test
    fun `Map villigTilÅTaImotTilbudOmArbeid verdi `() {
        Assertions.assertThat(arbeidssøker?.villigTilÅTaImotTilbudOmArbeid?.verdi).isTrue()
    }

    @Test
    fun `Map kanDuBegynneInnenEnUke label `() {
        Assertions.assertThat((arbeidssøker?.kanDuBegynneInnenEnUke?.label))
                .isEqualTo("Kan du begynne i arbeid senest én uke etter at du har fått tilbud om jobb?")
    }

    @Test
    fun `Map kanDuBegynneInnenEnUke verdi `() {
        Assertions.assertThat(arbeidssøker?.kanDuBegynneInnenEnUke?.verdi).isTrue()
    }

    @Test
    fun `Map kanDuSkaffeBarnepassInnenEnUke label `() {
        Assertions.assertThat((arbeidssøker?.kanDuSkaffeBarnepassInnenEnUke?.label))
                .isEqualTo("Har du eller kan du skaffe barnepass senest innen en uke etter at du har fått tilbud om jobb eller arbeidsmarkedtiltak?")
    }

    @Test
    fun `Map kanDuSkaffeBarnepassInnenEnUke verdi `() {
        Assertions.assertThat(arbeidssøker?.kanDuSkaffeBarnepassInnenEnUke?.verdi).isTrue()
    }

    @Test
    fun `Map hvorØnskerDuArbeid label `() {
        Assertions.assertThat((arbeidssøker?.hvorØnskerDuArbeid?.label))
                .isEqualTo("Hvor ønsker du å søke arbeid?")
    }

    @Test
    fun `Map hvorØnskerDuArbeid verdi `() {
        Assertions.assertThat(arbeidssøker?.hvorØnskerDuArbeid?.verdi).isEqualTo("Hvor som helst i landet")
    }

    @Test
    fun `Map ønskerDuMinst50ProsentStilling label `() {
        Assertions.assertThat((arbeidssøker?.ønskerDuMinst50ProsentStilling?.label))
                .isEqualTo("Ønsker du å stå som arbeidssøker til minst 50% stilling?")
    }

    @Test
    fun `Map ønskerDuMinst50ProsentStilling verdi `() {
        Assertions.assertThat(arbeidssøker?.ønskerDuMinst50ProsentStilling?.verdi).isTrue()
    }


}