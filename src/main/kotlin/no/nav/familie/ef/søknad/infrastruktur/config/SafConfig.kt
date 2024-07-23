package no.nav.familie.ef.søknad.infrastruktur.config

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Configuration
class SafConfig(
    @Value("\${SAF_URL}") safUrl: URI,
) {
    val safRestUri: URI =
        UriComponentsBuilder
            .fromUri(safUrl)
            .pathSegment(PATH_REST)
            .build()
            .toUri()
    val safGraphQLUri: URI =
        UriComponentsBuilder
            .fromUri(safUrl)
            .pathSegment(PATH_GRAPHQL)
            .build()
            .toUri()

    companion object {
        private const val PATH_GRAPHQL = "graphql"
        private const val PATH_REST = "rest"

        val safQuery = graphqlQuery("/saf/hentJournalposter.graphql")

        private fun graphqlQuery(path: String) =
            SafConfig::class.java
                .getResource(path)
                .readText()
                .graphqlCompatible()

        private fun String.graphqlCompatible(): String = StringUtils.normalizeSpace(this.replace("\n", ""))
    }
}
