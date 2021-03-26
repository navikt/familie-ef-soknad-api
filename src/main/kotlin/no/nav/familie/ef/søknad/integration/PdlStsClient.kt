package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.PdlConfig
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlAnnenForelder
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBolkResponse
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlPersonBolkRequest
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlPersonBolkRequestVariables
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlPersonRequest
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlPersonRequestVariables
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlResponse
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

    fun hentAnnenForelder(annenForelderIdent: String): PdlAnnenForelder {

        val pdlPersonRequest = PdlPersonRequest(variables = PdlPersonRequestVariables(annenForelderIdent),
                                                query = PdlConfig.annenForelderQuery)
        val pdlResponse: PdlResponse<PdlAnnenForelder> = postForEntity(pdlConfig.pdlUri,
                                                                       pdlPersonRequest,
                                                                       httpHeaders())
        return feilsjekkOgReturnerData(annenForelderIdent, pdlResponse) { it }

    }


    open class PdlRequestException(melding: String? = null) : Exception(melding)
    class PdlNotFoundException : PdlRequestException()


    private inline fun <reified DATA : Any, reified T : Any> feilsjekkOgReturnerData(ident: String,
                                                                                     pdlResponse: PdlResponse<DATA>,
                                                                                     dataMapper: (DATA) -> T?): T {

        if (pdlResponse.harFeil()) {
            if (pdlResponse.errors?.any { it.extensions?.notFound() == true } == true) {
                throw PdlNotFoundException()
            }
            secureLogger.error("Feil ved henting av ${T::class} fra PDL: ${pdlResponse.errorMessages()}")
            throw PdlRequestException("Feil ved henting av ${T::class} fra PDL. Se secure logg for detaljer.")
        }

        val data = dataMapper.invoke(pdlResponse.data)
        if (data == null) {
            secureLogger.error("Feil ved oppslag på ident $ident. " +
                               "PDL rapporterte ingen feil men returnerte tomt datafelt")
            throw PdlRequestException("Manglende ${T::class} ved feilfri respons fra PDL. Se secure logg for detaljer.")
        }
        return data
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
