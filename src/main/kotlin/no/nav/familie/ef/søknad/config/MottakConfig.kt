package no.nav.familie.ef.søknad.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

// TODO Endres til ConfigurationProperties ved release av SpringBoot 2.2.0
//@ConfigurationProperties(prefix = "søknad", ignoreUnknownFields = false)
@Component
data class MottakConfig(@Value("\${familie.ef.mottak.url}") val uri: String)
