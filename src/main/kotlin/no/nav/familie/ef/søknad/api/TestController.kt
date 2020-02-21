package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.integration.AbstractRestClient
import no.nav.security.token.support.core.api.Unprotected
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestOperations
import java.net.URI


@RestController
@RequestMapping(path = ["/api/test"], produces = [APPLICATION_JSON_VALUE])
@Unprotected
class TestController(operations: RestOperations) : AbstractRestClient(operations){

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping()
    fun sendInn(): String {
        val uri =
                URI("https://login.microsoftonline.com/NAVtestB2C.onmicrosoft.com/v2.0/.well-known/openid-configuration?p=B2C_1A_idporten_ver1")
        val forEntity = getForEntity<String>(uri)
        logger.info(forEntity);
        return forEntity
    }
}
