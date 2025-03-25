package no.nav.familie.ef.søknad.søknad

import no.nav.familie.ef.søknad.infrastruktur.config.MottakConfig
import no.nav.familie.ef.søknad.søknad.SøknadClientUtil.filtrerVekkEldreDokumentasjonsbehov
import no.nav.familie.ef.søknad.søknad.dto.KvitteringDto
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
import no.nav.familie.kontrakter.felles.søknad.SistInnsendtSøknadDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class MottakClient(
    private val config: MottakConfig,
    @Qualifier("tokenExchange") operations: RestOperations,
) : AbstractPingableRestClient(operations, "søknad.innsending") {
    override val pingUri: URI = config.pingUri

    fun sendInnEttersending(ettersending: Map<StønadType, EttersendelseDto>): KvitteringDto = postForEntity(config.sendInnEttersendingUri, ettersending)

    fun hentSøknaderMedDokumentasjonsbehov(personIdent: String): List<SøknadMedDokumentasjonsbehovDto> {
        val søknaderMedDokumentasjonsbehov: List<SøknadMedDokumentasjonsbehovDto> =
            postForEntity(
                config.hentSøknaderMedDokumentasjonsbehovUri,
                PersonIdent(personIdent),
                HttpHeaders().medContentTypeJsonUTF8(),
            )
        return filtrerVekkEldreDokumentasjonsbehov(søknaderMedDokumentasjonsbehov)
    }

    fun hentEttersendingForPerson(personIdent: String): List<EttersendelseDto> =
        postForEntity(
            config.hentEttersendingForPersonUri,
            PersonIdent(personIdent),
            HttpHeaders().medContentTypeJsonUTF8(),
        )

    fun sendInnSøknadOvergangsstønad(søknadMedVedlegg: SøknadMedVedlegg<SøknadOvergangsstønad>): KvitteringDto = postForEntity(config.sendInnOvergangsstønadKvitteringUri, søknadMedVedlegg)

    fun sendInnSøknadBarnetilsyn(søknadMedVedlegg: SøknadMedVedlegg<SøknadBarnetilsyn>): KvitteringDto = postForEntity(config.sendInnBarnetilsynKvitteringUri, søknadMedVedlegg)

    fun sendInnSøknadSkolepenger(søknadMedVedlegg: SøknadMedVedlegg<SøknadSkolepenger>): KvitteringDto = postForEntity(config.sendInnSkolepengerKvitteringUri, søknadMedVedlegg)

    fun sendInnArbeidssøkerSkjema(skjema: SkjemaForArbeidssøker): KvitteringDto = postForEntity(config.sendInnArbeidssøkerSkjemaKvitteringUri, skjema)

    fun hentForrigeBarnetilsynSøknad(): SøknadBarnetilsyn? =
        getForEntity(
            config.hentForrigeBarnetilsynSøknadUri,
            HttpHeaders().medContentTypeJsonUTF8(),
        )

    fun hentForrigeBarnetilsynSøknadKvittering(): SøknadBarnetilsyn? =
        getForEntity(
            config.hentForrigeBarnetilsynSøknadUriKvittering,
            HttpHeaders().medContentTypeJsonUTF8(),
        )

    fun hentSistInnsendtSøknadPerStønad(): List<SistInnsendtSøknadDto> =
        getForEntity(
            config.hentSistInnsendtSøknadPerStønad,
            HttpHeaders().medContentTypeJsonUTF8(),
        )

    fun hentSøknadKvittering(søknadId: String): ByteArray =
        getForEntity<ByteArray>(
            UriComponentsBuilder
                .fromUriString(
                    "${config.hentSøknadKvitteringUri}/$søknadId",
                ).build()
                .toUri(),
            HttpHeaders().medContentTypeJsonUTF8(),
        )

    private fun HttpHeaders.medContentTypeJsonUTF8(): HttpHeaders {
        this.add("Content-Type", "application/json;charset=UTF-8")
        this.add("behandlingsnummer", Tema.ENF.behandlingsnummer)
        this.acceptCharset = listOf(Charsets.UTF_8)
        return this
    }
}
