package no.nav.familie.ef.søknad.infrastruktur.config

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Configuration
class SafConfig(@Value("\${SAF_URL}") safUrl: URI) {

    val safUri: URI = UriComponentsBuilder.fromUri(safUrl).pathSegment(PATH_GRAPHQL).build().toUri()

    companion object {

        const val PATH_GRAPHQL = "graphql"

        val safQuery = graphqlQuery("/saf/hentJournalposter.graphql")

        private fun graphqlQuery(path: String) = SafConfig::class.java.getResource(path)
            .readText()
            .graphqlCompatible()

        private fun String.graphqlCompatible(): String {
            return StringUtils.normalizeSpace(this.replace("\n", ""))
        }
    }
}
