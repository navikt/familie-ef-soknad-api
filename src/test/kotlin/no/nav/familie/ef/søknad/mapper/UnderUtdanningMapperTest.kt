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
    val underUtdanning = aktivitetMapped.underUtdanning?.verdi

    @Test
    fun `Map underUtdanning er mappet `() {
        Assertions.assertThat(underUtdanning).isNotNull()
    }

    @Test
    fun `Map arbeidsforhold sluttdato verdi `() {
        Assertions.assertThat(underUtdanning?.utdanningEtterGrunnskolen?.verdi).isTrue()
    }

    @Test
    fun `Map arbeidsforhold sluttdato label `() {
        val expected = "Har du tatt utdanning etter grunnskolen"
        Assertions.assertThat(underUtdanning?.utdanningEtterGrunnskolen?.label).isEqualTo(expected)
    }

    @Test
    fun `Map arbeidsforhold linjeKursGrad verdi `() {
        Assertions.assertThat(underUtdanning?.gjeldendeUtdanning?.verdi?.linjeKursGrad?.verdi).isEqualTo("Stor kurs grad")
    }

    @Test
    fun `Map arbeidsforhold linjeKursGrad label `() {
        Assertions.assertThat(underUtdanning?.gjeldendeUtdanning?.verdi?.linjeKursGrad?.label).isEqualTo("Linje / kurs / grad")
    }

    @Test
    fun `Map arbeidsforhold hvaErMåletMedUtdanningen verdi `() {
        Assertions.assertThat(underUtdanning?.hvaErMåletMedUtdanningen?.verdi).isEqualTo("Bli flink")
    }

    @Test
    fun `Map arbeidsforhold hvaErMåletMedUtdanningen label `() {
        Assertions.assertThat(underUtdanning?.hvaErMåletMedUtdanningen?.label).isEqualTo("Hva er målet med utdanningen?")
    }

    @Test
    fun `Map arbeidsforhold offentligEllerPrivat verdi `() {
        Assertions.assertThat(underUtdanning?.offentligEllerPrivat?.verdi).isEqualTo("Offentlig")
    }

    @Test
    fun `Map arbeidsforhold offentligEllerPrivat label `() {
        Assertions.assertThat(underUtdanning?.offentligEllerPrivat?.label).isEqualTo("Er utdanningen privat eller offentlig?")
    }

    @Test
    fun `Map arbeidsforhold hvorMyeSkalDuStudere verdi `() {
        Assertions.assertThat(underUtdanning?.hvorMyeSkalDuStudere?.verdi).isEqualTo(50)
    }

    @Test
    fun `Map arbeidsforhold hvorMyeSkalDuStudere label `() {
        Assertions.assertThat(underUtdanning?.hvorMyeSkalDuStudere?.label).isEqualTo("Hvor mye skal du studere?")
    }

    @Test
    fun `Map arbeidsforhold skoleUtdanningssted verdi `() {
        Assertions.assertThat(underUtdanning?.skoleUtdanningssted?.verdi).isEqualTo("Skoleskolen")
    }

    @Test
    fun `Map arbeidsforhold skoleUtdanningssted label `() {
        Assertions.assertThat(underUtdanning?.skoleUtdanningssted?.label).isEqualTo("Skole / utdanningssted")
    }

    @Test
    fun `Map arbeidsforhold tidligereUtdanninger verdi `() {
        Assertions.assertThat(underUtdanning?.tidligereUtdanninger?.verdi).hasSize(1)
    }

    @Test
    fun `Map eksamensgebyr og semesteravgift`() {
        Assertions.assertThat(underUtdanning?.semesteravgift).isNull()
        Assertions.assertThat(underUtdanning?.eksamensgebyr?.verdi).isEqualTo(2000.0)
        Assertions.assertThat(underUtdanning?.studieavgift).isNull()
    }
}
