package no.nav.familie.ef.soknad.mapper

import no.nav.familie.ef.soknad.api.dto.KvitteringDto
import no.nav.familie.ef.soknad.integration.dto.Kvittering
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KvitteringMapperTest {

    @Test
    fun `mapTilEkstern returnere kvitteringDto basert på kvittering`() {
        val kvittering = Kvittering("Dette er en kvittering")
        val forventetKvitteringDto = KvitteringDto("Dette er en kvittering")

        val kvitteringDto = KvitteringMapper.mapTilEkstern(kvittering)

        assertThat(kvitteringDto).isEqualTo(forventetKvitteringDto)
    }

    @Test
    fun `mapTilEkstern returnere tom kvitteringDto hvis kvittering er null`() {
        val forventetKvitteringDto = KvitteringDto("")

        val kvitteringDto = KvitteringMapper.mapTilEkstern(null)

        assertThat(kvitteringDto).isEqualTo(forventetKvitteringDto)
    }

}