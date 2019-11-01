package no.nav.familie.ef.søknad.util

import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

object URIUtil {

    fun uri(base: URI, path: String): URI = UriComponentsBuilder
            .fromUri(base)
            .pathSegment(path)
            .build()
            .toUri()
}
