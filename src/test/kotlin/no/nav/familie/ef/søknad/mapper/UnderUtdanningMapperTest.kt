package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.AktivitetsMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class UnderUtdanningMapperTest {

    // Gitt
    private val aktivitet = søknadDto().aktivitet

    // Når
    private val aktivitetMapped = AktivitetsMapper.map(aktivitet, mapOf()).verdi
    val uderUtdanning = aktivitetMapped.underUtdanning?.verdi

    @Test
    fun `Map underUtdanning er mappet `() {
        Assertions.assertThat(uderUtdanning).isNotNull()
    }

    @Test
    fun `Map arbeidsforhold sluttdato verdi `() {
        Assertions.assertThat(uderUtdanning?.utdanningEtterGrunnskolen?.verdi).isTrue()
    }

    @Test
    fun `Map arbeidsforhold sluttdato label `() {
        val expected = "Har du tatt utdanning etter grunnskolen"
        Assertions.assertThat(uderUtdanning?.utdanningEtterGrunnskolen?.label).isEqualTo(expected)
    }

    @Test
    fun `Map arbeidsforhold linjeKursGrad verdi `() {
        Assertions.assertThat(uderUtdanning?.utdanning?.verdi?.linjeKursGrad?.verdi).isEqualTo("Stor kurs grad")
    }

    @Test
    fun `Map arbeidsforhold linjeKursGrad label `() {
        Assertions.assertThat(uderUtdanning?.utdanning?.verdi?.linjeKursGrad?.label).isEqualTo("Linje / kurs / grad")
    }

    @Test
    fun `Map arbeidsforhold hvaErMåletMedUtdanningen verdi `() {
        Assertions.assertThat(uderUtdanning?.hvaErMåletMedUtdanningen?.verdi).isEqualTo("Bli flink")
    }

    @Test
    fun `Map arbeidsforhold hvaErMåletMedUtdanningen label `() {
        Assertions.assertThat(uderUtdanning?.hvaErMåletMedUtdanningen?.label).isEqualTo("Hva er målet med utdanningen?")
    }

    @Test
    fun `Map arbeidsforhold offentligEllerPrivat verdi `() {
        Assertions.assertThat(uderUtdanning?.offentligEllerPrivat?.verdi).isEqualTo("Offentlig")
    }

    @Test
    fun `Map arbeidsforhold offentligEllerPrivat label `() {
        Assertions.assertThat(uderUtdanning?.offentligEllerPrivat?.label).isEqualTo("Er utdanningen privat eller offentlig?")
    }

    @Test
    fun `Map arbeidsforhold hvorMyeSkalDuStudere verdi `() {
        Assertions.assertThat(uderUtdanning?.hvorMyeSkalDuStudere?.verdi).isEqualTo(50)
    }

    @Test
    fun `Map arbeidsforhold hvorMyeSkalDuStudere label `() {
        Assertions.assertThat(uderUtdanning?.hvorMyeSkalDuStudere?.label).isEqualTo("Hvor mye skal du studere?")
    }

    @Test
    fun `Map arbeidsforhold skoleUtdanningssted verdi `() {
        Assertions.assertThat(uderUtdanning?.skoleUtdanningssted?.verdi).isEqualTo("Skoleskolen")
    }

    @Test
    fun `Map arbeidsforhold skoleUtdanningssted label `() {
        Assertions.assertThat(uderUtdanning?.skoleUtdanningssted?.label).isEqualTo("Skole / utdanningssted")
    }

    @Test
    fun `Map arbeidsforhold tidligereUtdanninger verdi `() {
        Assertions.assertThat(uderUtdanning?.tidligereUtdanninger?.verdi).hasSize(1)
    }



}