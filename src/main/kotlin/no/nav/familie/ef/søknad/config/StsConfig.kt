package no.nav.familie.ef.søknad.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.net.URI

@Configuration
class StsConfig(@Value("\${STS_URL}") val uri: URI,
                @Value("\${sts.passord}") val passord: String)