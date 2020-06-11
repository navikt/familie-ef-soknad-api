package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.AktivitetsMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class ArbeidsforholdMapperTest {

    // Gitt
    private val søknadDto = søknadDto()

    // Når
    private val aktivitet = AktivitetsMapper.map(søknadDto)
    private val arbeidsforhold = aktivitet.arbeidsforhold?.verdi?.get(0)

    @Test
    fun `Map arbeidsforhold arbeidsgivernavn label `() {
        Assertions.assertThat(arbeidsforhold?.arbeidsgivernavn?.label).isEqualTo("Navn på arbeidsgiver")
    }

    @Test
    fun `Map arbeidsforhold arbeidsgivernavn verdi `() {
        Assertions.assertThat(arbeidsforhold?.arbeidsgivernavn?.verdi).isEqualTo("Nav")
    }

    @Test
    fun `Map arbeidsforhold fastEllerMidlertidig label `() {
        Assertions.assertThat(arbeidsforhold?.fastEllerMidlertidig?.label).isEqualTo("Hva slags ansettelsesforhold har du?")
    }

    @Test
    fun `Map arbeidsforhold fastEllerMidlertidig verdi `() {
        Assertions.assertThat(arbeidsforhold?.fastEllerMidlertidig?.verdi).isEqualTo("Fast")
    }

    @Test
    fun `Map arbeidsforhold arbeidsmengde label `() {
        Assertions.assertThat(arbeidsforhold?.arbeidsmengde?.label).isEqualTo("Hvor mye jobber du?")
    }

    @Test
    fun `Map arbeidsforhold arbeidsmengde verdi `() {
        Assertions.assertThat(arbeidsforhold?.arbeidsmengde?.verdi).isEqualTo(23)
    }

    @Test
    fun `Map arbeidsforhold harSluttdato label `() {
        Assertions.assertThat(arbeidsforhold?.harSluttdato?.label).isEqualTo("Har du en sluttdato?")
    }

    @Test
    fun `Map arbeidsforhold harSluttdato verdi `() {
        Assertions.assertThat(arbeidsforhold?.harSluttdato?.verdi).isEqualTo(true)
    }

    @Test
    fun `Map arbeidsforhold sluttdato label `() {
        Assertions.assertThat(arbeidsforhold?.sluttdato?.label).isEqualTo("Når skal du slutte?")
    }

    @Test
    fun `Map arbeidsforhold sluttdato verdi `() {
        Assertions.assertThat(arbeidsforhold?.sluttdato?.verdi).isEqualTo(LocalDate.of(2020, 3, 31))
    }


}