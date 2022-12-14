package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.EttersendingMapper
import no.nav.familie.kontrakter.ef.ettersending.Dokumentasjonsbehov
import no.nav.familie.kontrakter.ef.ettersending.EttersendelseDto
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import no.nav.familie.kontrakter.felles.ef.StønadType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

internal class EttersendingMapperTest {

    @Test
    internal fun `skal mappe en liste med innsendinger per stønadstype`() {
        val skolepengedokumentasjon = lagDokumentasjonsbehov(stønadType = StønadType.SKOLEPENGER)
        val barnetilsynDokumentasjon = lagDokumentasjonsbehov(stønadType = StønadType.BARNETILSYN)
        val overgangsstønadDokumentasjon1 = lagDokumentasjonsbehov(stønadType = StønadType.OVERGANGSSTØNAD)
        val overgangsstønadDokumentasjon2 = lagDokumentasjonsbehov(stønadType = StønadType.OVERGANGSSTØNAD)
        val overgangsstønadDokumentasjon3 = lagDokumentasjonsbehov(stønadType = StønadType.OVERGANGSSTØNAD)

        val personIdent = "12345678901"
        val innsendingMottatt = LocalDateTime.now()
        val nyttMap =
            EttersendingMapper.groupByStønad(
                dto = EttersendelseDto(
                    dokumentasjonsbehov = listOf(
                        skolepengedokumentasjon,
                        barnetilsynDokumentasjon,
                        overgangsstønadDokumentasjon1,
                        overgangsstønadDokumentasjon2,
                        overgangsstønadDokumentasjon3
                    ),
                    personIdent = personIdent
                ),
                innsendingMottatt = innsendingMottatt
            )
        assertThat(nyttMap).containsKey(StønadType.OVERGANGSSTØNAD)
        assertThat(nyttMap).containsKey(StønadType.BARNETILSYN)
        assertThat(nyttMap).containsKey(StønadType.SKOLEPENGER)
        assertThat(nyttMap[StønadType.OVERGANGSSTØNAD]?.dokumentasjonsbehov).hasSize(3)
        assertThat(nyttMap[StønadType.BARNETILSYN]?.dokumentasjonsbehov).hasSize(1)
        assertThat(nyttMap[StønadType.SKOLEPENGER]?.dokumentasjonsbehov).hasSize(1)
        assertThat(nyttMap[StønadType.SKOLEPENGER]?.personIdent).isEqualTo(personIdent)
        assertThat(nyttMap[StønadType.BARNETILSYN]?.personIdent).isEqualTo(personIdent)
        assertThat(nyttMap[StønadType.OVERGANGSSTØNAD]?.personIdent).isEqualTo(personIdent)
        assertThat(nyttMap[StønadType.SKOLEPENGER]?.dokumentasjonsbehov?.first()?.innsendingstidspunkt).isEqualTo(innsendingMottatt)
        assertThat(nyttMap[StønadType.BARNETILSYN]?.dokumentasjonsbehov?.first()?.innsendingstidspunkt).isEqualTo(innsendingMottatt)
        assertThat(nyttMap[StønadType.OVERGANGSSTØNAD]?.dokumentasjonsbehov).allMatch { it.innsendingstidspunkt == innsendingMottatt }
    }

    private fun lagDokumentasjonsbehov(stønadType: StønadType) = Dokumentasjonsbehov(
        id = UUID.randomUUID().toString(),
        søknadsdata = null,
        dokumenttype = "DOKUMENTASJON_PÅ_SYKDOM",
        beskrivelse = "Dokumentasjon på sykdom",
        stønadType = stønadType,
        innsendingstidspunkt = LocalDateTime.now()
            .minusYears(1),
        vedlegg = listOf(
            Vedlegg(
                id = "1",
                navn = "Vedlegg 1",
                tittel = "Dokumentasjon på sykdom"
            )
        )
    )
}
