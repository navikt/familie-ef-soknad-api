package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.PdlConfig
import no.nav.familie.ef.søknad.exception.PdlRequestException
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlAnnenForelder
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBolkResponse
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlPersonBolkRequest
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlPersonBolkRequestVariables
import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.http.sts.StsRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations


@Component
class PdlStsClient(val pdlConfig: PdlConfig,
                   @Qualifier("stsRestKlientMedApiKey") restOperations: RestOperations,
                   private val stsRestClient: StsRestClient)
    : AbstractRestClient(restOperations, "pdl.personinfo") {

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add("Nav-Consumer-Token", "Bearer ${stsRestClient.systemOIDCToken}")
            add("Tema", "ENF")
        }
    }


    fun hentBarn(personIdenter: List<String>): Map<String, PdlBarn> {
        if (personIdenter.isEmpty()) return emptyMap()
        val pdlPersonRequest = PdlPersonBolkRequest(variables = PdlPersonBolkRequestVariables(personIdenter),
                                                    query = PdlConfig.barnQuery)
        val pdlResponse: PdlBolkResponse<PdlBarn> = postForEntity(pdlConfig.pdlUri,
                                                                  pdlPersonRequest,
                                                                  httpHeaders())
        return feilsjekkOgReturnerData(pdlResponse)
    }


    fun hentAndreForeldre(personIdenter: List<String>): Map<String, PdlAnnenForelder> {
        if (personIdenter.isEmpty()) return emptyMap()
        val pdlPersonRequest = PdlPersonBolkRequest(variables = PdlPersonBolkRequestVariables(personIdenter),
                                                    query = PdlConfig.annenForelderQuery)
        val pdlResponse: PdlBolkResponse<PdlAnnenForelder> = postForEntity(pdlConfig.pdlUri,
                                                                           pdlPersonRequest,
                                                                           httpHeaders())
        return feilsjekkOgReturnerData(pdlResponse)
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
        return pdlResponse.data.personBolk.associateBy({ it.ident }, { it.person!! })
    }


}
