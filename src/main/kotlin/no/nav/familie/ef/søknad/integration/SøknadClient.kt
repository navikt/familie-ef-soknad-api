package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.integration.SøknadClientUtil.filtrerVekkEldreDokumentasjonsbehov
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.kontrakter.ef.ettersending.EttersendelseDto
import no.nav.familie.kontrakter.ef.ettersending.SøknadMedDokumentasjonsbehovDto
import no.nav.familie.kontrakter.ef.søknad.SkjemaForArbeidssøker
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg
import no.nav.familie.kontrakter.ef.søknad.SøknadOvergangsstønad
import no.nav.familie.kontrakter.ef.søknad.SøknadSkolepenger
import no.nav.familie.kontrakter.felles.PersonIdent
import no.nav.familie.kontrakter.felles.Tema
import no.nav.familie.kontrakter.felles.ef.StønadType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import java.net.URI

@Service
class SøknadClient(
    private val config: MottakConfig,
    @Qualifier("tokenExchange") operations: RestOperations,
) :
    AbstractPingableRestClient(operations, "søknad.innsending") {

    override val pingUri: URI = config.pingUri

    fun sendInn(søknadMedVedlegg: SøknadMedVedlegg<SøknadOvergangsstønad>): KvitteringDto {
        return postForEntity(config.sendInnOvergangsstønadUri, søknadMedVedlegg)
    }

    fun sendInnBarnetilsynsøknad(søknadMedVedlegg: SøknadMedVedlegg<SøknadBarnetilsyn>): KvitteringDto {
        return postForEntity(config.sendInnBarnetilsynUri, søknadMedVedlegg)
    }

    fun sendInnSkolepenger(søknadMedVedlegg: SøknadMedVedlegg<SøknadSkolepenger>): KvitteringDto {
        return postForEntity(config.sendInnSkolepengerUri, søknadMedVedlegg)
    }

    fun sendInnEttersending(ettersending: Map<StønadType, EttersendelseDto>): KvitteringDto {
        return postForEntity(config.sendInnEttersendingUri, ettersending)
    }

    fun sendInnArbeidsRegistreringsskjema(skjema: SkjemaForArbeidssøker): KvitteringDto {
        return postForEntity(config.sendInnSkjemaArbeidUri, skjema)
    }

    fun hentSøknaderMedDokumentasjonsbehov(personIdent: String): List<SøknadMedDokumentasjonsbehovDto> {
        val søknaderMedDokumentasjonsbehov: List<SøknadMedDokumentasjonsbehovDto> =
            postForEntity(
                config.hentSøknaderMedDokumentasjonsbehovUri,
                PersonIdent(personIdent),
                HttpHeaders().medContentTypeJsonUTF8(),
            )
        return filtrerVekkEldreDokumentasjonsbehov(søknaderMedDokumentasjonsbehov)
    }

    fun hentEttersendingForPerson(personIdent: String): List<EttersendelseDto> {
        return postForEntity(
            config.hentEttersendingForPersonUri,
            PersonIdent(personIdent),
            HttpHeaders().medContentTypeJsonUTF8(),
        )
    }

    fun hentDataTilGjenbrukBarnetilsyn(): SøknadBarnetilsyn {
        return getForEntity(
            config.hentBarnetilsynSøknadUri,
            HttpHeaders().medContentTypeJsonUTF8(),
        )
    }

    private fun HttpHeaders.medContentTypeJsonUTF8(): HttpHeaders {
        this.add("Content-Type", "application/json;charset=UTF-8")
        this.add("behandlingsnummer", Tema.ENF.behandlingsnummer)
        this.acceptCharset = listOf(Charsets.UTF_8)
        return this
    }
}
