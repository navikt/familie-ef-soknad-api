package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

internal class SkjemaMapperTest {

    val hvorØnskerSøkerArbeid: TekstFelt = TekstFelt("label", "Hvorsomhelst", "hvasomhelst")
    val kanBegynneInnenEnUke: BooleanFelt = BooleanFelt("label", false)
    val kanSkaffeBarnepassInnenEnUke: BooleanFelt = BooleanFelt("label", false)
    val registrertSomArbeidssøkerNav: BooleanFelt = BooleanFelt("label", false)
    val villigTilÅTaImotTilbudOmArbeid: BooleanFelt = BooleanFelt("label", false)
    val ønskerSøker50ProsentStilling: BooleanFelt = BooleanFelt("label", false)
    val arbeidssøker = Arbeidssøker(
        hvorØnskerSøkerArbeid,
        kanBegynneInnenEnUke,
        kanSkaffeBarnepassInnenEnUke,
        registrertSomArbeidssøkerNav,
        villigTilÅTaImotTilbudOmArbeid,
        ønskerSøker50ProsentStilling
    )

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @Test
    fun `Map til kontrakt fungerer når alle verdier er ok `() {
        val forventetFnr = "14108921464" // gyldig, syntetisk fnr
        val kontrakt = SkjemaMapper.mapTilKontrakt(arbeidssøker, forventetFnr, "Innsenders navn", innsendingMottatt)
        Assertions.assertThat(kontrakt.personaliaArbeidssøker.verdi.fødselsnummer.verdi.verdi).isEqualTo(forventetFnr)
    }

    @Test
    fun `Navn blir mappet`() {
        val forventetNavn = "Innsenders navn"
        val kontrakt = SkjemaMapper.mapTilKontrakt(arbeidssøker, "14108921464", forventetNavn, innsendingMottatt)
        Assertions.assertThat(kontrakt.personaliaArbeidssøker.verdi.navn.verdi).isEqualTo(forventetNavn)
    }

    @Test
    fun `Map til kontrakt feiler med ugyldig fnr`() {
        val ikkeGyldigFnr = "12345678910"
        assertFailsWith(IllegalStateException::class) {
            SkjemaMapper.mapTilKontrakt(arbeidssøker, ikkeGyldigFnr, "Innsenders navn", innsendingMottatt)
        }
    }

    @Test
    fun `Map til kontrakt mapper riktig verdi - hvorØnskerSøkerArbeid`() {
        val kontrakt = SkjemaMapper.mapTilKontrakt(arbeidssøker, "14108921464", "Innsenders navn", innsendingMottatt)
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.hvorØnskerDuArbeid.verdi)
            .isEqualTo(arbeidssøker.hvorØnskerSøkerArbeid.verdi)
    }

    @Test
    fun `Map til kontrakt mapper riktig verdi - kanBegynneInnenEnUke`() {
        val copy = arbeidssøker.copy(kanBegynneInnenEnUke = BooleanFelt("Endret label", true))
        // When
        val kontrakt = SkjemaMapper.mapTilKontrakt(copy, "14108921464", "Innsenders navn", innsendingMottatt)
        // Then
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.verdi).isEqualTo(true)
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.label).isEqualTo("Endret label")
    }

    @Test
    fun `Map til kontrakt mapper riktig verdi - kanSkaffeBarnepassInnenEnUke`() {
        val copy = arbeidssøker.copy(kanBegynneInnenEnUke = BooleanFelt("kanSkaffeBarnepassInnenEnUke", true))
        // When
        val kontrakt = SkjemaMapper.mapTilKontrakt(copy, "14108921464", "Innsenders navn", innsendingMottatt)
        // Then
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.verdi).isEqualTo(true)
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.label).isEqualTo("kanSkaffeBarnepassInnenEnUke")
    }

    @Test
    fun `Map til kontrakt mapper riktig verdi - registrertSomArbeidssøkerNav`() {
        val copy = arbeidssøker.copy(kanBegynneInnenEnUke = BooleanFelt("registrertSomArbeidssøkerNav", true))
        // When
        val kontrakt = SkjemaMapper.mapTilKontrakt(copy, "14108921464", "Innsenders navn", innsendingMottatt)
        // Then
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.verdi).isEqualTo(true)
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.label).isEqualTo("registrertSomArbeidssøkerNav")
    }

    @Test
    fun `Map til kontrakt mapper riktig verdi - villigTilÅTaImotTilbudOmArbeid`() {
        val copy = arbeidssøker.copy(kanBegynneInnenEnUke = BooleanFelt("villigTilÅTaImotTilbudOmArbeid", true))
        // When
        val kontrakt = SkjemaMapper.mapTilKontrakt(copy, "14108921464", "Innsenders navn", innsendingMottatt)
        // Then
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.verdi).isEqualTo(true)
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.label)
            .isEqualTo("villigTilÅTaImotTilbudOmArbeid")
    }

    @Test
    fun `Map til kontrakt mapper riktig verdi - ønskerSøker50ProsentStilling`() {
        val copy = arbeidssøker.copy(kanBegynneInnenEnUke = BooleanFelt("ønskerSøker50ProsentStilling", true))
        // When
        val kontrakt = SkjemaMapper.mapTilKontrakt(copy, "14108921464", "Innsenders navn", innsendingMottatt)
        // Then
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.verdi).isEqualTo(true)
        Assertions.assertThat(kontrakt.arbeidssøker.verdi.kanDuBegynneInnenEnUke.label).isEqualTo("ønskerSøker50ProsentStilling")
    }
}
