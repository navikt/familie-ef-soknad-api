package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.integration.AbstractRestClient
import no.nav.security.token.support.core.api.Unprotected
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestOperations
import java.net.URI
import java.net.URL


@Service class TestClientA (@Qualifier("restOperationsNoInterceptors")restOperationsNoInterceptors: RestOperations): AbstractRestClient(restOperationsNoInterceptors){fun getForUri( uri: URI): String = getForEntity(uri)}
@Service class TestClientB ( @Qualifier("restOperations")operations: RestOperations): AbstractRestClient(operations){fun getForUri( uri: URI): String = getForEntity(uri)}

@RestController
@RequestMapping(path = ["/api/"], produces = [APPLICATION_JSON_VALUE])
@Unprotected
class TestController(private val testClientA: TestClientA, private val testClientB: TestClientB) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("testA")
    fun sendInn(): String? {
        val uri =
                URI("https://login.microsoftonline.com/NAVtestB2C.onmicrosoft.com/v2.0/.well-known/openid-configuration?p=B2C_1A_idporten_ver1")
        val forEntity = testClientA.getForUri(uri)
        logger.info(forEntity);
         return  forEntity
    }

    @GetMapping("testB")
    fun test2 (): String? {
         return URL("https://login.microsoftonline.com/NAVtestB2C.onmicrosoft.com/v2.0/.well-known/openid-configuration?p=B2C_1A_idporten_ver1").readText()
    }

    @GetMapping("testC")
    fun testC (): String? {
        val uri =
                URI("https://login.microsoftonline.com/NAVtestB2C.onmicrosoft.com/v2.0/.well-known/openid-configuration?p=B2C_1A_idporten_ver1")
        val forEntity = testClientB.getForUri(uri)
        logger.info(forEntity);
        return  forEntity
    }
}

