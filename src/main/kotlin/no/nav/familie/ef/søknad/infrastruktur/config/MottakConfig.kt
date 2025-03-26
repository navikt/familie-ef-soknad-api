package no.nav.familie.ef.søknad.infrastruktur.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@ConfigurationProperties("familie.ef.mottak")
data class MottakConfig(
    val uri: URI,
) {
    internal val sendInnOvergangsstønadKvitteringUri = byggUri(PATH_SEND_INN_SØKNAD_OVERGANGSSTØNAD)
    internal val sendInnBarnetilsynKvitteringUri = byggUri(PATH_SEND_INN_SØKNAD_BARNETILSYN)
    internal val sendInnSkolepengerKvitteringUri = byggUri(PATH_SEND_INN_SØKNAD_SKOLEPENGER)
    internal val sendInnArbeidssøkerSkjemaKvitteringUri = byggUri(PATH_SEND_INN_ARBEIDSSOKER_SKJEMA)

    internal val hentSøknaderMedDokumentasjonsbehovUri = byggUri(PATH_HENT_SØKNADER)
    internal val sendInnEttersendingUri = byggUri(PATH_SEND_INN_ETTERSENDING)
    internal val hentEttersendingForPersonUri = byggUri(PATH_HENT_ETTERSENDING_FOR_PERSON)
    internal val hentForrigeBarnetilsynSøknadUri = byggUri(PATH_HENT_FORRIGE_BARNETILSYNSØKNAD)
    internal val hentForrigeBarnetilsynSøknadUriKvittering = byggUri(PATH_HENT_FORRIGE_BARNETILSYNSØKNAD_KVITTERING)
    internal val hentSøknadKvitteringUri = byggUri(PATH_HENT_SOKNADKVITTERING)

    internal val hentSistInnsendtSøknadPerStønad = byggUri(PATH_HENT_SIST_INNSENDT_SØKNAD_PER_STØNAD)

    internal val pingUri = byggUri(PATH_PING)

    private fun byggUri(path: String) =
        UriComponentsBuilder
            .fromUri(uri)
            .path(path)
            .build()
            .toUri()

    companion object {
        private const val PATH_SEND_INN_SØKNAD_OVERGANGSSTØNAD = "/soknadskvittering/overgangsstonad"
        private const val PATH_SEND_INN_SØKNAD_BARNETILSYN = "/soknadskvittering/barnetilsyn"
        private const val PATH_SEND_INN_SØKNAD_SKOLEPENGER = "/soknadskvittering/skolepenger"
        private const val PATH_SEND_INN_ARBEIDSSOKER_SKJEMA = "/soknadskvittering/arbeidssoker"
        private const val PATH_HENT_SØKNADER = "/person/soknader"
        private const val PATH_SEND_INN_ETTERSENDING = "/ettersending"
        private const val PATH_HENT_ETTERSENDING_FOR_PERSON = "/ettersending/person"
        private const val PATH_HENT_FORRIGE_BARNETILSYNSØKNAD = "/soknad/barnetilsyn/forrige"
        private const val PATH_HENT_FORRIGE_BARNETILSYNSØKNAD_KVITTERING = "/soknadskvittering/barnetilsyn/forrige"
        private const val PATH_PING = "/ping"
        private const val PATH_HENT_SOKNADKVITTERING = "/soknadskvittering"
        private const val PATH_HENT_SIST_INNSENDT_SØKNAD_PER_STØNAD = "/soknad/sist-innsendt-per-stonad"
    }
}
