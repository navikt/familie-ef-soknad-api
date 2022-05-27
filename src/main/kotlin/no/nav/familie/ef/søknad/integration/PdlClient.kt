package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.PdlConfig
import no.nav.familie.ef.søknad.exception.PdlRequestException
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlPersonRequest
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlPersonRequestVariables
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlResponse
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlSøker
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlSøkerData
import no.nav.familie.http.client.AbstractPingableRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import java.net.URI

@Component
class PdlClient(
    val pdlConfig: PdlConfig,
    @Qualifier("tokenExchange") restOperations: RestOperations
) :
    AbstractPingableRestClient(restOperations, "pdl.personinfo") {

    fun hentSøker(personIdent: String): PdlSøker {
        val pdlPersonRequest = PdlPersonRequest(
            variables = PdlPersonRequestVariables(personIdent),
            query = PdlConfig.søkerQuery
        )
        val pdlResponse: PdlResponse<PdlSøkerData> = postForEntity(
            pdlConfig.pdlUri,
            pdlPersonRequest,
            httpHeaders()
        )
        return feilsjekkOgReturnerData(personIdent, pdlResponse) { it.person }
    }

    /**
     * dataMapper håndterer att både data i PdlResponse og eks person i PdlSøker er null
     */
    private inline fun <reified DATA : Any, reified T : Any> feilsjekkOgReturnerData(
        ident: String,
        pdlResponse: PdlResponse<DATA>,
        dataMapper: (DATA) -> T?
    ): T {
        if (pdlResponse.harFeil()) {
            secureLogger.error("Feil ved henting av ${T::class} fra PDL: ${pdlResponse.errorMessages()}")
            throw PdlRequestException("Feil ved henting av ${T::class} fra PDL. Se secure logg for detaljer.")
        }

        val data = dataMapper.invoke(pdlResponse.data)
        if (data == null) {
            secureLogger.error(
                "Feil ved oppslag på ident $ident. " +
                    "PDL rapporterte ingen feil men returnerte tomt datafelt"
            )
            throw PdlRequestException("Manglende ${T::class} ved feilfri respons fra PDL. Se secure logg for detaljer.")
        }
        return data
    }

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add("Tema", "ENF")
        }
    }

    override val pingUri: URI
        get() = pdlConfig.pdlUri

    override fun ping() {
        operations.optionsForAllow(pingUri)
    }
}
