package no.nav.familie.ef.søknad.infrastruktur.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenXDecoder: TokenXDecoder,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/internal/**",
                        "/api/ping",
                        "/api/featuretoggle/**",
                    ).permitAll()
                it.anyRequest().authenticated()
            }.oauth2ResourceServer { it.jwt { jwt -> jwt.decoder(tokenXDecoder) } }
            .build()
}
