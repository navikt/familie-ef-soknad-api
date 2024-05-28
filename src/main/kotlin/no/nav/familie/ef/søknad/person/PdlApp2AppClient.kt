package no.nav.familie.ef.søknad.person

import no.nav.familie.ef.søknad.infrastruktur.config.PdlConfig
import no.nav.familie.ef.søknad.infrastruktur.exception.PdlRequestException
import no.nav.familie.ef.søknad.person.dto.PdlAnnenForelder
import no.nav.familie.ef.søknad.person.dto.PdlBarn
import no.nav.familie.ef.søknad.person.dto.PdlBolkResponse
import no.nav.familie.ef.søknad.person.dto.PdlPersonBolkRequest
import no.nav.familie.ef.søknad.person.dto.PdlPersonBolkRequestVariables
import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.kontrakter.felles.Tema
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
class PdlApp2AppClient(
    val pdlConfig: PdlConfig,
    @Qualifier("clientCredential") restOperations: RestOperations,
) :
    AbstractRestClient(restOperations, "pdl.personinfo") {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun hentBarn(personIdenter: List<String>): Map<String, PdlBarn> {
        if (personIdenter.isEmpty()) return emptyMap()
        val pdlPersonRequest =
            PdlPersonBolkRequest(
                variables = PdlPersonBolkRequestVariables(personIdenter),
                query = PdlConfig.barnQuery,
            )
        val pdlResponse: PdlBolkResponse<PdlBarn> =
            postForEntity(
                pdlConfig.pdlUri,
                pdlPersonRequest,
                httpHeaders(),
            )
        return feilsjekkOgReturnerData(pdlResponse)
    }

    fun hentAndreForeldre(personIdenter: List<String>): Map<String, PdlAnnenForelder> {
        if (personIdenter.isEmpty()) return emptyMap()
        val pdlPersonRequest =
            PdlPersonBolkRequest(
                variables = PdlPersonBolkRequestVariables(personIdenter),
                query = PdlConfig.annenForelderQuery,
            )
        val pdlResponse: PdlBolkResponse<PdlAnnenForelder> =
            postForEntity(
                pdlConfig.pdlUri,
                pdlPersonRequest,
                httpHeaders(),
            )
        return feilsjekkOgReturnerData(pdlResponse)
    }

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add("Tema", "ENF")
            add("behandlingsnummer", Tema.ENF.behandlingsnummer)
        }
    }

    private inline fun <reified T : Any> feilsjekkOgReturnerData(pdlResponse: PdlBolkResponse<T>): Map<String, T> {
        if (pdlResponse.data == null) {
            secureLogger.error("Data fra pdl er null ved bolkoppslag av ${T::class} fra PDL: ${pdlResponse.errorMessages()}")
            throw PdlRequestException("Data er null fra PDL -  ${T::class}. Se secure logg for detaljer.")
        }

        val feil = pdlResponse.data.personBolk.filter { it.code != "ok" }.map { it.ident to it.code }.toMap()
        if (feil.isNotEmpty()) {
            secureLogger.error("Feil ved henting av ${T::class} fra PDL: $feil")
            throw PdlRequestException("Feil ved henting av ${T::class} fra PDL. Se secure logg for detaljer.")
        }
        if (pdlResponse.harAdvarsel()) {
            logger.warn("Advarsel ved henting av ${T::class} fra PDL. Se securelogs for detaljer.")
            secureLogger.warn("Advarsel ved henting av ${T::class} fra PDL: ${pdlResponse.extensions?.warnings}")
        }
        return pdlResponse.data.personBolk.associateBy({ it.ident }, { it.person!! })
    }
}
