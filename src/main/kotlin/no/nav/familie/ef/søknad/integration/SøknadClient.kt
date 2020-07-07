package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.http.client.MultipartBuilder
import no.nav.familie.kontrakter.ef.søknad.SkjemaForArbeidssøker
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import java.net.URI


@Service
class SøknadClient(private val config: MottakConfig,
                   operations: RestOperations) : AbstractPingableRestClient(operations, "søknad.innsending") {

    override val pingUri: URI = config.pingUri

    fun sendInn(søknadRequestData: SøknadRequestData): KvitteringDto {
        val multipartBuilder = MultipartBuilder().withJson("søknad", søknadRequestData.søknadMedVedlegg)
        søknadRequestData.vedlegg.forEach { multipartBuilder.withByteArray("vedlegg", it.key, it.value) }
        return postForEntity(config.sendInnUri, multipartBuilder.build(), MultipartBuilder.MULTIPART_HEADERS)
    }

    fun sendInnArbeidsRegistreringsskjema(skjema: SkjemaForArbeidssøker): KvitteringDto {
        return postForEntity(config.sendInnSkjemaArbeidUri, skjema)
    }

}
