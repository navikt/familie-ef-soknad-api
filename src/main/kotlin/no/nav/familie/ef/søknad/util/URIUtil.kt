package no.nav.familie.ef.søknad.util

import org.springframework.http.HttpHeaders
import org.springframework.web.util.UriComponentsBuilder

import java.net.URI

object URIUtil {

    fun uri(base: URI, path: String, queryParams: HttpHeaders? = null): URI {
        return builder(base, path, queryParams)
                .build()
                .toUri()
    }

    fun builder(base: URI, path: String, queryParams: HttpHeaders?): UriComponentsBuilder {
        return UriComponentsBuilder
                .fromUri(base)
                .pathSegment(path)
                .queryParams(queryParams)
    }

    fun queryParams(vararg pairs: Pair<String, Any>): HttpHeaders {
        val queryParams = HttpHeaders()
        for (pair in pairs) {
            queryParams.add(pair.first, pair.second.toString())
        }
        return queryParams
    }
}
