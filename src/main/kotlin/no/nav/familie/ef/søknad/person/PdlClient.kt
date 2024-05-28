package no.nav.familie.ef.søknad.person

import no.nav.familie.ef.søknad.infrastruktur.config.PdlConfig
import no.nav.familie.ef.søknad.infrastruktur.exception.PdlRequestException
import no.nav.familie.ef.søknad.person.dto.PdlPersonRequest
import no.nav.familie.ef.søknad.person.dto.PdlPersonRequestVariables
import no.nav.familie.ef.søknad.person.dto.PdlResponse
import no.nav.familie.ef.søknad.person.dto.PdlSøker
import no.nav.familie.ef.søknad.person.dto.PdlSøkerData
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.kontrakter.felles.Tema
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import java.net.URI

@Component
class PdlClient(
    val pdlConfig: PdlConfig,
    @Qualifier("tokenExchange") restOperations: RestOperations,
) :
    AbstractPingableRestClient(restOperations, "pdl.personinfo") {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun hentSøker(personIdent: String): PdlSøker {
        val pdlPersonRequest =
            PdlPersonRequest(
                variables = PdlPersonRequestVariables(personIdent),
                query = PdlConfig.søkerQuery,
            )
        val pdlResponse: PdlResponse<PdlSøkerData> =
            postForEntity(
                pdlConfig.pdlUri,
                pdlPersonRequest,
                httpHeaders(),
            )
        return feilsjekkOgReturnerData(personIdent, pdlResponse) { it.person }
    }

    /**
     * dataMapper håndterer att både data i PdlResponse og eks person i PdlSøker er null
     */
    private inline fun <reified DATA : Any, reified T : Any> feilsjekkOgReturnerData(
        ident: String,
        pdlResponse: PdlResponse<DATA>,
        dataMapper: (DATA) -> T?,
    ): T {
        if (pdlResponse.harFeil()) {
            secureLogger.error("Feil ved henting av ${T::class} fra PDL: ${pdlResponse.errorMessages()}")
            throw PdlRequestException("Feil ved henting av ${T::class} fra PDL. Se secure logg for detaljer.")
        }
        if (pdlResponse.harAdvarsel()) {
            logger.warn("Advarsel ved henting av ${T::class} fra PDL. Se securelogs for detaljer.")
            secureLogger.warn("Advarsel ved henting av ${T::class} fra PDL: ${pdlResponse.extensions?.warnings}")
        }
        val data = dataMapper.invoke(pdlResponse.data)
        if (data == null) {
            secureLogger.error(
                "Feil ved oppslag på ident $ident. " +
                    "PDL rapporterte ingen feil men returnerte tomt datafelt",
            )
            throw PdlRequestException("Manglende ${T::class} ved feilfri respons fra PDL. Se secure logg for detaljer.")
        }
        return data
    }

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add("Tema", "ENF")
            add("behandlingsnummer", Tema.ENF.behandlingsnummer)
        }
    }

    override val pingUri: URI
        get() = pdlConfig.pdlUri

    override fun ping() {
        operations.optionsForAllow(pingUri)
    }
}
