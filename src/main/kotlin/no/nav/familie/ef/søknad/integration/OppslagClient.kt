package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.api.dto.AktørId
import no.nav.familie.ef.søknad.config.OppslagConfig
import no.nav.familie.ef.søknad.integration.dto.PersonDto
import no.nav.familie.ef.søknad.integration.dto.SøkerinfoDto
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
class OppslagClient(operations: RestOperations, private val config: OppslagConfig) : AbstractRestClient(operations) {

    fun ping(): String = getForEntity(config.pingUri)

    fun hentPerson(): PersonDto {
        val person: PersonDto = getForEntity(config.personURI)
        log.info(confidential, "Fikk person {}", person)
        return person
    }

    fun hentSøkerInfo(): SøkerinfoDto {
        val info: SøkerinfoDto = getForEntity(config.søkerinfoURI)
        log.info(confidential, "Fikk søkerinfo {}", info)
        return info
    }

    fun hentAktørId(fnr: String): AktørId {
        return getForEntity(config.buildAktørIdURI(fnr))
    }




}
