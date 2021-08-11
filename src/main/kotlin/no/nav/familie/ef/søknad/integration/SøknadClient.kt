package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.http.client.MultipartBuilder
import no.nav.familie.kontrakter.ef.ettersending.EttersendingResponseData
import no.nav.familie.kontrakter.ef.ettersending.SøknadMedDokumentasjonsbehovDto
import no.nav.familie.kontrakter.ef.søknad.SkjemaForArbeidssøker
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.SøknadOvergangsstønad
import no.nav.familie.kontrakter.ef.søknad.SøknadSkolepenger
import no.nav.familie.kontrakter.ef.søknad.dokumentasjonsbehov.DokumentasjonsbehovDto
import no.nav.familie.kontrakter.felles.PersonIdent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import java.net.URI
import java.util.UUID


@Service
class SøknadClient(private val config: MottakConfig,
                   @Qualifier("tokenExchange") operations: RestOperations,
                   private val featureToggleService: FeatureToggleService)
    : AbstractPingableRestClient(operations, "søknad.innsending") {

    override val pingUri: URI = config.pingUri

    fun sendInn(søknadRequestData: SøknadRequestData<SøknadOvergangsstønad>): KvitteringDto {
        return postForEntity(config.sendInnOvergangsstønadUri, søknadRequestData)
    }

    fun sendInnBarnetilsynsøknad(søknadRequestData: SøknadRequestData<SøknadBarnetilsyn>): KvitteringDto {
        return postForEntity(config.sendInnBarnetilsynUri, søknadRequestData)
    }

    fun sendInnSkolepenger(søknadRequestData: SøknadRequestData<SøknadSkolepenger>): KvitteringDto {
        return postForEntity(config.sendInnSkolepengerUri, søknadRequestData)
    }

    fun sendInnEttersending(ettersendingRequestData: EttersendingRequestData): KvitteringDto {
        return postForEntity(config.sendInnEttersendingUri, ettersendingRequestData)
    }

    fun sendInnOld(søknadRequestData: SøknadRequestData<SøknadOvergangsstønad>): KvitteringDto {
        val multipartBuilder = MultipartBuilder().withJson("søknad", søknadRequestData.søknadMedVedlegg)
        søknadRequestData.vedlegg.forEach { multipartBuilder.withByteArray("vedlegg", it.key, it.value) }
        return postForEntity(config.sendInnOvergangsstønadUri, multipartBuilder.build(), MultipartBuilder.MULTIPART_HEADERS)
    }

    fun sendInnBarnetilsynsøknadOld(søknadRequestData: SøknadRequestData<SøknadBarnetilsyn>): KvitteringDto {
        val multipartBuilder = MultipartBuilder().withJson("søknad", søknadRequestData.søknadMedVedlegg)
        søknadRequestData.vedlegg.forEach { multipartBuilder.withByteArray("vedlegg", it.key, it.value) }
        return postForEntity(config.sendInnBarnetilsynUri, multipartBuilder.build(), MultipartBuilder.MULTIPART_HEADERS)
    }

    fun sendInnSkolepengerOld(søknadRequestData: SøknadRequestData<SøknadSkolepenger>): KvitteringDto {
        val multipartBuilder = MultipartBuilder().withJson("søknad", søknadRequestData.søknadMedVedlegg)
        søknadRequestData.vedlegg.forEach { multipartBuilder.withByteArray("vedlegg", it.key, it.value) }
        return postForEntity(config.sendInnSkolepengerUri, multipartBuilder.build(), MultipartBuilder.MULTIPART_HEADERS)
    }

    fun sendInnEttersendingOld(ettersendingRequestData: EttersendingRequestData): KvitteringDto {
        val multipartBuilder = MultipartBuilder().withJson("ettersending", ettersendingRequestData.ettersendingMedVedlegg)
        ettersendingRequestData.vedlegg.forEach { multipartBuilder.withByteArray("vedlegg", it.key, it.value) }
        return postForEntity(config.sendInnEttersendingUri, multipartBuilder.build(), MultipartBuilder.MULTIPART_HEADERS)
    }

    fun sendInnArbeidsRegistreringsskjema(skjema: SkjemaForArbeidssøker): KvitteringDto {
        return postForEntity(config.sendInnSkjemaArbeidUri, skjema)
    }

    fun hentDokumentasjonsbehovForSøknad(søknadId: UUID): DokumentasjonsbehovDto {
        return getForEntity(config.byggUriForDokumentasjonsbehov(søknadId))
    }

    fun hentSøknaderMedDokumentasjonsbehov(personIdent: String): List<SøknadMedDokumentasjonsbehovDto> {
        return postForEntity(config.hentSøknaderMedDokumentasjonsbehovUri,
                             PersonIdent(personIdent),
                             HttpHeaders().medContentTypeJsonUTF8())
    }

    fun hentEttersendingForPerson(personIdent: String): List<EttersendingResponseData> {
        return postForEntity(config.hentEttersendingForPersonUri,
                             PersonIdent(personIdent),
                             HttpHeaders().medContentTypeJsonUTF8())
    }

    private fun HttpHeaders.medContentTypeJsonUTF8(): HttpHeaders {
        this.add("Content-Type", "application/json;charset=UTF-8")
        this.acceptCharset = listOf(Charsets.UTF_8)
        return this
    }

    private fun <T, R> sendInn(request: T, v1: (T) -> R, v2: (T) -> R): R {
        return if (featureToggleService.isEnabled("familie.ef.soknad.api.dokumentFraMottak")) {
            v2.invoke(request)
        } else {
            v1.invoke(request)
        }
    }

}
