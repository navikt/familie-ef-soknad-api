package no.nav.familie.ef.søknad.config

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Configuration
class PdlConfig(@Value("\${PDL_URL}") pdlUrl: URI) {

    val pdlUri: URI = UriComponentsBuilder.fromUri(pdlUrl).pathSegment(PATH_GRAPHQL).build().toUri()

    companion object {

        const val PATH_GRAPHQL = "graphql"

        val søkerQuery = graphqlQuery("/pdl/søker.graphql")

        val barnQuery = graphqlQuery("/pdl/barn.graphql")

        val annenForelderQuery = graphqlQuery("/pdl/andreForeldre.graphql")

        private fun graphqlQuery(path: String) = PdlConfig::class.java.getResource(path)
            .readText()
            .graphqlCompatible()

        private fun String.graphqlCompatible(): String {
            return StringUtils.normalizeSpace(this.replace("\n", ""))
        }
    }
}
