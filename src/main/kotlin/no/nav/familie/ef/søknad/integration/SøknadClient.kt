package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.integration.SøknadClientUtil.filtrerVekkEldreDokumentasjonsbehov
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.kontrakter.ef.ettersending.EttersendelseDto
import no.nav.familie.kontrakter.ef.ettersending.SøknadMedDokumentasjonsbehovDto
import no.nav.familie.kontrakter.ef.felles.StønadType
import no.nav.familie.kontrakter.ef.søknad.SkjemaForArbeidssøker
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
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
                   private val featureToggleService: FeatureToggleService,
                   @Qualifier("tokenExchange") operations: RestOperations)
    : AbstractPingableRestClient(operations, "søknad.innsending") {

    override val pingUri: URI = config.pingUri

    fun config(): MottakConfig {
        if (featureToggleService.isEnabled("familie.ef.soknad.api.disabled")) {
            throw Exception("Familie-ef-soknad-api er disabled i forbindelse med migrering")
        }
        return if (featureToggleService.isEnabled("familie.ef.soknad.api.gcp")) {
            MottakConfig(URI.create("http://familie-ef-mottak/api"))
        } else {
            config
        }
    }

    fun sendInn(søknadMedVedlegg: SøknadMedVedlegg<SøknadOvergangsstønad>): KvitteringDto {
        return postForEntity(config().sendInnOvergangsstønadUri, søknadMedVedlegg)
    }

    fun sendInnBarnetilsynsøknad(søknadMedVedlegg: SøknadMedVedlegg<SøknadBarnetilsyn>): KvitteringDto {
        return postForEntity(config().sendInnBarnetilsynUri, søknadMedVedlegg)
    }

    fun sendInnSkolepenger(søknadMedVedlegg: SøknadMedVedlegg<SøknadSkolepenger>): KvitteringDto {
        return postForEntity(config().sendInnSkolepengerUri, søknadMedVedlegg)
    }

    fun sendInnEttersending(ettersending: Map<StønadType, EttersendelseDto>): KvitteringDto {
        return postForEntity(config().sendInnEttersendingUri, ettersending)
    }

    fun sendInnArbeidsRegistreringsskjema(skjema: SkjemaForArbeidssøker): KvitteringDto {
        return postForEntity(config().sendInnSkjemaArbeidUri, skjema)
    }

    fun hentDokumentasjonsbehovForSøknad(søknadId: UUID): DokumentasjonsbehovDto {
        return getForEntity(config().byggUriForDokumentasjonsbehov(søknadId))
    }

    fun hentSøknaderMedDokumentasjonsbehov(personIdent: String): List<SøknadMedDokumentasjonsbehovDto> {
        val søknaderMedDokumentasjonsbehov: List<SøknadMedDokumentasjonsbehovDto> =
                postForEntity(config().hentSøknaderMedDokumentasjonsbehovUri,
                              PersonIdent(personIdent),
                              HttpHeaders().medContentTypeJsonUTF8())
        return filtrerVekkEldreDokumentasjonsbehov(søknaderMedDokumentasjonsbehov)
    }

    fun hentEttersendingForPerson(personIdent: String): List<EttersendelseDto> {
        return postForEntity(config().hentEttersendingForPersonUri,
                             PersonIdent(personIdent),
                             HttpHeaders().medContentTypeJsonUTF8())
    }

    private fun HttpHeaders.medContentTypeJsonUTF8(): HttpHeaders {
        this.add("Content-Type", "application/json;charset=UTF-8")
        this.acceptCharset = listOf(Charsets.UTF_8)
        return this
    }

}
