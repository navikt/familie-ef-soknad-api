package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@ConfigurationProperties("familie.ef.mottak")
@ConstructorBinding
data class MottakConfig(val uri: URI) {

    internal val sendInnOvergangsstønadUri = byggUri(PATH_SEND_INN_OVERGANGSSTØNAD)
    internal val sendInnSkjemaArbeidUri = byggUri(PATH_SEND_INN_ARBEIDS_SKJEMA)
    internal val sendInnBarnetilsynUri = byggUri(PATH_SEND_INN_BARNETILSYNSØKNAD)
    internal val sendInnSkolepengerUri = byggUri(PATH_SEND_INN_SKOLEPENGERSØKNAD)

    internal val hentSøknaderMedDokumentasjonsbehovUri = byggUri(PATH_HENT_SØKNADER)
    internal val sendInnEttersendingUri = byggUri(PATH_SEND_INN_ETTERSENDING)
    internal val hentEttersendingForPersonUri = byggUri(PATH_HENT_ETTERSENDING_FOR_PERSON)

    internal val pingUri = byggUri(PATH_PING)

    private fun byggUri(path: String) = UriComponentsBuilder.fromUri(uri).path(path).build().toUri()

    companion object {

        private const val PATH_SEND_INN_OVERGANGSSTØNAD = "/soknad/overgangsstonad"
        private const val PATH_SEND_INN_ARBEIDS_SKJEMA = "/skjema"
        private const val PATH_SEND_INN_BARNETILSYNSØKNAD = "/soknad/barnetilsyn"
        private const val PATH_SEND_INN_SKOLEPENGERSØKNAD = "/soknad/skolepenger"
        private const val PATH_HENT_SØKNADER = "/person/soknader"
        private const val PATH_SEND_INN_ETTERSENDING = "/ettersending"
        private const val PATH_HENT_ETTERSENDING_FOR_PERSON = "/ettersending/person"
        private const val PATH_PING = "/ping"
    }
}
