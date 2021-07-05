package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.*

@ConfigurationProperties("familie.ef.mottak")
@ConstructorBinding
data class MottakConfig(val uri: URI,
                        val passord: String) {

    internal val sendInnOvergangsstønadUri = byggUri(PATH_SEND_INN_OVERGANGSSTØNAD)
    internal val sendInnSkjemaArbeidUri = byggUri(PATH_SEND_INN_ARBEIDS_SKJEMA)
    internal val sendInnBarnetilsynUri = byggUri(PATH_SEND_INN_BARNETILSYNSØKNAD)
    internal val sendInnSkolepengerUri = byggUri(PATH_SEND_INN_SKOLEPENGERSØKNAD)

    internal val hentSøknaderForPersonUri = byggUri(PATH_HENT_SØKNADER_FOR_PERSON)

    internal val pingUri = byggUri(PATH_PING)

    private fun byggUri(path: String) = UriComponentsBuilder.fromUri(uri).path(path).build().toUri()

    fun byggUriForDokumentasjonsbehov(søknadId: UUID) = UriComponentsBuilder.fromUri(uri).path(
            "$PATH_HENT_DOKUMENTASJONSBEHOV/$søknadId").build().toUri()

    companion object {
        private const val PATH_SEND_INN_OVERGANGSSTØNAD = "/soknad" //TODO endre til soknad/overgangsstonad senere
        private const val PATH_SEND_INN_ARBEIDS_SKJEMA = "/skjema"
        private const val PATH_SEND_INN_BARNETILSYNSØKNAD = "/soknad/barnetilsyn"
        private const val PATH_SEND_INN_SKOLEPENGERSØKNAD = "/soknad/skolepenger"
        private const val PATH_HENT_DOKUMENTASJONSBEHOV = "/soknad/dokumentasjonsbehov"
        private const val PATH_HENT_SØKNADER_FOR_PERSON = "/person/soknader"
        private const val PATH_PING = "/ping"
    }

}
