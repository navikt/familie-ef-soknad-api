package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KvitteringDtoMapperTest {

    @Test
    fun `mapTilEkstern returnerer kvitteringDto basert på kvittering`() {
        val kvittering = KvitteringDto("Dette er en kvittering")
        val forventetKvitteringDto = Kvittering("Dette er en kvittering")

        val kvitteringDto = KvitteringMapper.mapTilEkstern(kvittering)

        assertThat(kvitteringDto).isEqualTo(forventetKvitteringDto)
    }

    @Test
    fun `mapTilEkstern returnerer tom kvitteringDto hvis kvittering er null`() {
        val forventetKvitteringDto = Kvittering("")

        val kvitteringDto = KvitteringMapper.mapTilEkstern(null)

        assertThat(kvitteringDto).isEqualTo(forventetKvitteringDto)
    }

}