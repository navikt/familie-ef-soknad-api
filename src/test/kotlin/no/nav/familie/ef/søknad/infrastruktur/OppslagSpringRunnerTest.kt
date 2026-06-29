package no.nav.familie.ef.søknad.infrastruktur

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.github.tomakehurst.wiremock.WireMockServer
import no.nav.familie.ef.søknad.ApplicationLocalTestLauncher
import no.nav.familie.kontrakter.felles.jsonMapper
import no.nav.familie.util.FnrGenerator
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpHeaders
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import java.util.UUID

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [ApplicationLocalTestLauncher::class])
@ActiveProfiles(
    "integrationtest",
    "mock-kodeverk",
    "mock-dokument",
    "mock-pdl",
    "mock-pdlApp2AppClient",
    "mock-mottak",
    "kodeverk-cache-test",
    "mock-saf",
    "mock-saksbehandling",
)
abstract class OppslagSpringRunnerTest {
    protected val listAppender = initLoggingEventListAppender()
    protected var loggingEvents: MutableList<ILoggingEvent> = listAppender.list

    val jackson2HttpMessageConverter = JacksonJsonHttpMessageConverter(jsonMapper)
    val restTemplate = RestTemplateBuilder().additionalMessageConverters(listOf(jackson2HttpMessageConverter) + RestTemplate().messageConverters).build()

    protected val headers = HttpHeaders()

    @Autowired private lateinit var applicationContext: ApplicationContext

    @LocalServerPort
    private var port: Int? = 0

    @AfterEach
    fun reset() {
        headers.clear()
        loggingEvents.clear()
        resetWiremockServers()
    }

    private fun resetWiremockServers() {
        applicationContext.getBeansOfType(WireMockServer::class.java).values.forEach(WireMockServer::resetRequests)
    }

    protected fun getPort(): String = port.toString()

    protected fun localhost(uri: String): String = LOCALHOST + getPort() + uri

    protected fun søkerBearerToken(personident: String = FnrGenerator.generer()): String = jwt(personident)

    private fun jwt(fnr: String) = mockOAuth2Server.token(subject = fnr)

    private fun MockOAuth2Server.token(
        subject: String,
        issuerId: String = "tokenx",
        clientId: String = UUID.randomUUID().toString(),
        audience: String = TOKEN_X_CLIENT_ID,
        claims: Map<String, Any> = mapOf("acr" to "Level4"),
    ): String =
        this
            .issueToken(
                issuerId,
                clientId,
                DefaultOAuth2TokenCallback(
                    issuerId = issuerId,
                    subject = subject,
                    audience = listOf(audience),
                    claims = claims,
                    expiry = 3600,
                ),
            ).serialize()

    companion object {
        private const val LOCALHOST = "http://localhost:"
        private const val TOKEN_X_CLIENT_ID = "familie-app"

        val mockOAuth2Server: MockOAuth2Server by lazy {
            MockOAuth2Server().also { it.start() }
        }

        @JvmStatic
        @DynamicPropertySource
        @Suppress("unused") // method is used via reflection by Spring's test framework (the @DynamicPropertySource annotation).
        fun mockOAuth2ServerProperties(registry: DynamicPropertyRegistry) {
            registry.add("TOKEN_X_ISSUER") { mockOAuth2Server.issuerUrl("tokenx").toString() }
            registry.add("TOKEN_X_CLIENT_ID") { TOKEN_X_CLIENT_ID }
            registry.add("TOKEN_X_JWKS_URI") { mockOAuth2Server.jwksUrl("tokenx").toString() }
        }

        protected fun initLoggingEventListAppender(): ListAppender<ILoggingEvent> {
            val listAppender = ListAppender<ILoggingEvent>()
            listAppender.start()
            return listAppender
        }
    }
}
