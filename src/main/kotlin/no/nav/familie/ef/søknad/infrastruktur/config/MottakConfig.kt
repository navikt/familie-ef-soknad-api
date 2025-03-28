package no.nav.familie.ef.søknad.infrastruktur.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@ConfigurationProperties("familie.ef.mottak")
data class MottakConfig(
    val uri: URI,
) {
    internal val sendInnSøknadOverganggstønadUri = byggUri(PATH_SEND_INN_SØKNAD_OVERGANGSSTØNAD)
    internal val sendInnSøknadBarnetilsynUri = byggUri(PATH_SEND_INN_SØKNAD_BARNETILSYN)
    internal val sendInnSøknadSkolepengerUri = byggUri(PATH_SEND_INN_SØKNAD_SKOLEPENGER)
    internal val sendInnSkjemaArbeidssøkerUri = byggUri(PATH_SEND_INN_SKJEMA_ARBEIDSSØKER)
    internal val hentForrigeSøknadBarnetilsynUri = byggUri(PATH_HENT_FORRIGE_SØKNAD_BARNETILSYN)
    internal val hentSistInnsendteSøknadPerStønadUri = byggUri(PATH_HENT_SIST_INNSENDTE_SØKNAD_PER_STØNAD)

    internal val sendInnEttersendingUri = byggUri(PATH_SEND_INN_ETTERSENDING)
    internal val hentEttersendingForPersonUri = byggUri(PATH_HENT_ETTERSENDING_FOR_PERSON)

    internal val hentSøknaderMedDokumentasjonsbehovUri = byggUri(PATH_HENT_SØKNADER)
    internal val pingUri = byggUri(PATH_PING)

    private fun byggUri(path: String) =
        UriComponentsBuilder
            .fromUri(uri)
            .path(path)
            .build()
            .toUri()

    companion object {
        private const val PATH_SEND_INN_SØKNAD_OVERGANGSSTØNAD = "/soknad/overgangsstonad"
        private const val PATH_SEND_INN_SØKNAD_BARNETILSYN = "/soknad/barnetilsyn"
        private const val PATH_SEND_INN_SØKNAD_SKOLEPENGER = "/soknad/skolepenger"
        private const val PATH_SEND_INN_SKJEMA_ARBEIDSSØKER = "/soknad/arbeidssoker"
        private const val PATH_HENT_FORRIGE_SØKNAD_BARNETILSYN = "/soknad/barnetilsyn/forrige"
        private const val PATH_HENT_SIST_INNSENDTE_SØKNAD_PER_STØNAD = "/soknad/sist-innsendt-per-stonad"
        private const val PATH_SEND_INN_ETTERSENDING = "/ettersending"
        private const val PATH_HENT_ETTERSENDING_FOR_PERSON = "/ettersending/person"
        private const val PATH_HENT_SØKNADER = "/person/soknader"
        private const val PATH_PING = "/ping"
    }
}
