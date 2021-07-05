package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.http.client.MultipartBuilder
import no.nav.familie.kontrakter.ef.søknad.SkjemaForArbeidssøker
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.SøknadOvergangsstønad
import no.nav.familie.kontrakter.ef.søknad.SøknadSkolepenger
import no.nav.familie.kontrakter.ef.søknad.dokumentasjonsbehov.DokumentasjonsbehovDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import java.net.URI
import java.util.*


@Service
class SøknadClient(private val config: MottakConfig,
                   @Qualifier("restKlientMedApiKey") operations: RestOperations)
    : AbstractPingableRestClient(operations, "søknad.innsending") {

    override val pingUri: URI = config.pingUri

    fun sendInn(søknadRequestData: SøknadRequestData<SøknadOvergangsstønad>): KvitteringDto {
        val multipartBuilder = MultipartBuilder().withJson("søknad", søknadRequestData.søknadMedVedlegg)
        søknadRequestData.vedlegg.forEach { multipartBuilder.withByteArray("vedlegg", it.key, it.value) }
        return postForEntity(config.sendInnOvergangsstønadUri, multipartBuilder.build(), MultipartBuilder.MULTIPART_HEADERS)
    }

    fun sendInnBarnetilsynsøknad(søknadRequestData: SøknadRequestData<SøknadBarnetilsyn>): KvitteringDto {
        val multipartBuilder = MultipartBuilder().withJson("søknad", søknadRequestData.søknadMedVedlegg)
        søknadRequestData.vedlegg.forEach { multipartBuilder.withByteArray("vedlegg", it.key, it.value) }
        return postForEntity(config.sendInnBarnetilsynUri, multipartBuilder.build(), MultipartBuilder.MULTIPART_HEADERS)
    }

    fun sendInnSkolepenger(søknadRequestData: SøknadRequestData<SøknadSkolepenger>): KvitteringDto {
        val multipartBuilder = MultipartBuilder().withJson("søknad", søknadRequestData.søknadMedVedlegg)
        søknadRequestData.vedlegg.forEach { multipartBuilder.withByteArray("vedlegg", it.key, it.value) }
        return postForEntity(config.sendInnSkolepengerUri, multipartBuilder.build(), MultipartBuilder.MULTIPART_HEADERS)
    }

    fun sendInnArbeidsRegistreringsskjema(skjema: SkjemaForArbeidssøker): KvitteringDto {
        return postForEntity(config.sendInnSkjemaArbeidUri, skjema)
    }

    fun hentDokumentasjonsbehovForSøknad(søknadId: UUID): DokumentasjonsbehovDto {
        return getForEntity(config.byggUriForDokumentasjonsbehov(søknadId))
    }

    fun hentSøknaderForPerson(personIdent: String): List<String> {
        return postForEntity(config.hentSøknaderForPersonUri, personIdent)
    }

}
