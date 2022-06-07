package no.nav.familie.ef.søknad.integrationTest
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.github.tomakehurst.wiremock.WireMockServer
import no.nav.familie.ef.søknad.ApplicationLocalLauncher
import no.nav.familie.util.FnrGenerator
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.security.token.support.core.context.TokenValidationContextHolder
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [ApplicationLocalLauncher::class])
@ActiveProfiles("local",
                "mock-kodeverk",
                "mock-dokument",
                "mock-pdl",
                "mock-pdlApp2AppClient",
                "mock-mottak",
                "kodeverk-cache-test")
@EnableMockOAuth2Server()
abstract class OppslagSpringRunnerTest {

    protected val listAppender = initLoggingEventListAppender()
    protected var loggingEvents: MutableList<ILoggingEvent> = listAppender.list
    protected val restTemplate = TestRestTemplate()
    protected val headers = HttpHeaders()


    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private lateinit var mockOAuth2Server: MockOAuth2Server

    // @Autowired private lateinit var jdbcAggregateOperations: JdbcAggregateOperations
    @Autowired private lateinit var applicationContext: ApplicationContext
   // @Autowired private lateinit var cacheManager: CacheManager
  //  @Autowired @Qualifier("kodeverkCache") private lateinit var cacheManagerKodeverk: CacheManager
   // @Autowired private lateinit var rolleConfig: RolleConfig


    @Autowired private lateinit var validationContext: TokenValidationContextHolder

    @LocalServerPort
    private var port: Int? = 0

    @AfterEach
    fun reset() {
        headers.clear()
        loggingEvents.clear()
//        clearCaches()
        resetWiremockServers()
    }

    private fun resetWiremockServers() {
        applicationContext.getBeansOfType(WireMockServer::class.java).values.forEach(WireMockServer::resetRequests)
    }

//    private fun clearCaches() {
//        listOf(cacheManagerKodeverk, cacheManager).forEach {
//            it.cacheNames.mapNotNull { cacheName -> it.getCache(cacheName) }
//                    .forEach { cache -> cache.clear() }
//        }
//    }



    protected fun getPort(): String {
        return port.toString()
    }

    protected fun localhost(uri: String): String {
        return LOCALHOST + getPort() + uri
    }

    fun jwt(fnr: String) = mockOAuth2Server.token(subject = fnr)


    fun MockOAuth2Server.token(
            subject: String,
            issuerId: String = "selvbetjening",
            clientId: String = UUID.randomUUID().toString(),
            audience: String = "aud-localhost",
            claims: Map<String, Any> = mapOf("acr" to "Level4"),

            ): String {
        return this.issueToken(
                issuerId,
                clientId
//                DefaultOAuth2TokenCallback(
//                        issuerId = issuerId,
//                        subject = subject,
//                        audience = listOf(audience),
//                        claims = claims,
//                        expiry = 3600
//                )
        ).serialize()
    }

    protected fun onBehalfOfToken(): String {
        return TokenUtil.onBehalfOfToken(mockOAuth2Server,  "sdf", "wfe")
    }

//    protected fun clientToken(clientId: String = "1", accessAsApplication: Boolean = false): String {
//        return TokenUtil.clientToken(mockOAuth2Server, clientId, accessAsApplication)
//    }

    protected fun søkerBearerToken(personident: String = FnrGenerator.generer()): String {
        return jwt(personident)
    }

    companion object {

        private const val LOCALHOST = "http://localhost:"
        protected fun initLoggingEventListAppender(): ListAppender<ILoggingEvent> {
            val listAppender = ListAppender<ILoggingEvent>()
            listAppender.start()
            return listAppender
        }
    }
}
