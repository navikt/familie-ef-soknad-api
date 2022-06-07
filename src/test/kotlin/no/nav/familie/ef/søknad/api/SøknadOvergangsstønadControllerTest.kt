//package no.nav.familie.ef.søknad.api
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import no.nav.familie.ef.søknad.ApplicationLocalLauncher
//import no.nav.familie.ef.søknad.api.dto.Kvittering
//import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Person
//import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
//import no.nav.familie.ef.søknad.mock.søkerMedDefaultVerdier
//import no.nav.familie.ef.søknad.mock.søknadDto
//import no.nav.familie.ef.søknad.service.SøknadService
//import no.nav.security.token.support.core.JwtTokenConstants
//import no.nav.security.token.support.test.JwtTokenGenerator
//import org.assertj.core.api.Assertions.assertThat
//import org.glassfish.jersey.logging.LoggingFeature
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//import org.springframework.context.annotation.Profile
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.junit.jupiter.SpringExtension
//import java.time.LocalDateTime
//import javax.ws.rs.client.ClientBuilder
//import javax.ws.rs.client.Entity
//import javax.ws.rs.core.MediaType
//
//@Profile("overgangsstonad-controller-test")
//@Configuration
//class SøknadOvergangsstønadControllerTestConfiguration {
//
//    @Primary
//    @Bean
//    fun søknadService(): SøknadService = mockk()
//
//    @Primary
//    @Bean
//    fun featureToggleService(): FeatureToggleService = mockk()
//}
//
//@ActiveProfiles("local", "overgangsstonad-controller-test")
//@ExtendWith(SpringExtension::class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [ApplicationLocalLauncher::class])
//internal class SøknadOvergangsstønadControllerTest {
//
//    private fun serializedJWTToken() = JwtTokenGenerator.createSignedJWT(tokenSubject).serialize()
//    @Autowired lateinit var søknadService: SøknadService
//    @Autowired lateinit var featureToggleService: FeatureToggleService
//
//    @Value("\${local.server.port}")
//    val port: Int = 0
//    val contextPath = "/api"
//    val tokenSubject = "12345678911"
//
//    @Test
//    fun `sendInn returnerer kvittering riktig kvittering med riktig Bearer token`() {
//        fun client() = ClientBuilder.newClient().register(LoggingFeature::class.java)
//        fun webTarget() = client().target("http://localhost:$port$contextPath")
//        val søknad = søknadOvergangsstønadDto(tokenSubject)
//        every { søknadService.sendInn(søknad, any()) } returns Kvittering("Mottatt søknad: $søknad", LocalDateTime.now())
//        every { featureToggleService.isEnabled(any()) } returns true
//        val response = webTarget().path("/soknad/overgangsstonad/")
//            .request(MediaType.APPLICATION_JSON)
//            .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer ${serializedJWTToken()}")
//            .post(Entity.json(søknad))
//        assertThat(response.status).isEqualTo(200)
//    }
//
//    private fun søknadOvergangsstønadDto(fnr: String) = søknadDto()
//        .copy(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = fnr), barn = listOf()))
//
//    @Test
//    fun `sendInn returnerer 403 ved ulik fnr i token og søknad`() {
//        fun client() = ClientBuilder.newClient().register(LoggingFeature::class.java)
//        fun webTarget() = client().target("http://localhost:$port$contextPath")
//        val søknad = søknadDto()
//        val response = webTarget().path("/soknad/overgangsstonad/")
//            .request(MediaType.APPLICATION_JSON)
//            .header(JwtTokenConstants.AUTHORIZATION_HEADER, "Bearer ${serializedJWTToken()}")
//            .post(Entity.json(søknad))
//        assertThat(response.status).isEqualTo(403)
//        verify(exactly = 0) { søknadService.sendInn(søknad, any()) }
//    }
//}
