package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.infrastruktur.config.SafConfig
import no.nav.familie.ef.søknad.minside.domain.DokumentoversiktSelvbetjening
import no.nav.familie.ef.søknad.minside.domain.Journalpost
import no.nav.familie.ef.søknad.minside.dto.SafDokumentOversiktRequest
import no.nav.familie.ef.søknad.minside.dto.SafDokumentOversiktResponse
import no.nav.familie.ef.søknad.minside.dto.SafDokumentVariables
import no.nav.familie.http.client.AbstractPingableRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import java.net.URI
import no.nav.familie.ef.søknad.minside.domain.DokumentoversiktSelvbetjeningResponse

@Component
class SafClient(
    val safConfig: SafConfig,
    @Qualifier("tokenExchange") restOperations: RestOperations,
) : AbstractPingableRestClient(restOperations, "saf.dokument") {

    fun hentJournalposterForBruker(personIdent: String): List<Journalpost> {
        val safDokumentRequest = SafDokumentOversiktRequest(
            variables = SafDokumentVariables(personIdent),
            query = SafConfig.safQuery,
        )

        val safDokumentResponse: SafDokumentOversiktResponse<DokumentoversiktSelvbetjeningResponse> =
            postForEntity(safConfig.safUri, safDokumentRequest)

        return feilsjekkOgReturnerData(
            personIdent,
            safDokumentResponse,
        ) { it.dokumentoversiktSelvbetjening.tema.find { tema -> tema.kode == "ENF" }?.journalposter ?: emptyList() }
    }

    private inline fun <reified DATA : Any, reified T : Any> feilsjekkOgReturnerData(
        ident: String,
        safResponse: SafDokumentOversiktResponse<DATA>,
        dataMapper: (DATA) -> T?,
    ): T {
        if (safResponse.harFeil()) {
            secureLogger.error("Feil ved henting av ${T::class} fra SAF: ${safResponse.errorMessages()}")
            throw IllegalStateException("Feil ved henting av ${T::class} fra PDL. Se secure logg for detaljer.")
        }
        val data = dataMapper.invoke(safResponse.data)
        if (data == null) {
            secureLogger.error(
                "Feil ved uthenting av dokumenter for ident $ident.",
            )
            throw IllegalStateException("Manglende ${T::class} ved feilfri respons fra SAF. Se secure logg for detaljer.")
        }
        return data
    }

    override val pingUri: URI
        get() = safConfig.safUri

    override fun ping() {
        operations.optionsForAllow(pingUri)
    }
}
