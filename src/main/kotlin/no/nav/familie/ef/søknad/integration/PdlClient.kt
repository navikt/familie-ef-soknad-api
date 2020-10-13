package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.sak.integration.dto.pdl.*
import no.nav.familie.ef.søknad.config.PdlConfig
import no.nav.familie.ef.søknad.exception.PdlRequestException
import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.http.sts.StsRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations


@Component
class PdlClient(val pdlConfig: PdlConfig,
                @Qualifier("sts") restOperations: RestOperations,
                private val stsRestClient: StsRestClient)
    : AbstractRestClient(restOperations, "pdl.personinfo") {

    fun hentSøker(personIdent: String): PdlSøker {
        val pdlPersonRequest = PdlPersonRequest(variables = PdlPersonRequestVariables(personIdent),
                                                query = PdlConfig.søkerQuery)
        val pdlResponse: PdlResponse<PdlSøkerData> = postForEntity(pdlConfig.pdlUri,
                                                                   pdlPersonRequest,
                                                                   httpHeaders())
        return feilsjekkOgReturnerData(personIdent, pdlResponse).person
    }

    private inline fun <reified T : Any> feilsjekkOgReturnerData(ident: String,
                                                                 pdlResponse: PdlResponse<T>): T {

        if (pdlResponse.harFeil()) {
            secureLogger.error("Feil ved henting av ${T::class} fra PDL: ${pdlResponse.errorMessages()}")
            throw PdlRequestException("Feil ved henting av ${T::class} fra PDL. Se secure logg for detaljer.")
        }

        if (pdlResponse.data == null) {
            secureLogger.error("Feil ved oppslag på ident $ident. " +
                               "PDL rapporterte ingen feil men returnerte tomt datafelt")
            throw PdlRequestException("Manglende ${T::class} ved feilfri respons fra PDL. Se secure logg for detaljer.")
        }
        return pdlResponse.data
    }

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add("Nav-Consumer-Token", "Bearer ${stsRestClient.systemOIDCToken}")
            add("Tema", "ENF")
        }
    }


}
