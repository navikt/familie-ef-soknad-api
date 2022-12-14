package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class KvitteringDtoMapperTest {

    private val innsendingMottatt = LocalDateTime.now()

    @Test
    fun `mapTilEkstern returnerer kvitteringDto basert på kvittering`() {
        val kvittering = KvitteringDto("Dette er en kvittering")
        val forventetKvitteringDto = Kvittering("Dette er en kvittering", innsendingMottatt)

        val kvitteringDto = KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)

        assertThat(kvitteringDto).isEqualTo(forventetKvitteringDto)
    }

    @Test
    fun `mapTilEkstern returnerer tom kvitteringDto hvis kvittering er null`() {
        val forventetKvitteringDto = Kvittering("", innsendingMottatt)

        val kvitteringDto = KvitteringMapper.mapTilEkstern(null, innsendingMottatt)

        assertThat(kvitteringDto).isEqualTo(forventetKvitteringDto)
    }

    @Test
    fun `mapTilEkstern returnerer kvittering med null hvis innsendingMottatt er null`() {
        val kvitteringDto = KvitteringMapper.mapTilEkstern(null, null)
        assertThat(kvitteringDto.mottattDato).isNull()
        assertThat(kvitteringDto.text).isEmpty()
    }
}
