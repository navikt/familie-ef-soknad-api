package no.nav.familie.ef.søknad.felles

import no.nav.security.mock.oauth2.MockOAuth2Server
import java.util.UUID

object TokenUtil {

    /**
     * client token
     * oid = unik id på applikasjon A i Azure AD
     * sub = unik id på applikasjon A i Azure AD, alltid lik oid
     */
    // client token har en oid som er den samme som sub
    fun clientToken(mockOAuth2Server: MockOAuth2Server, clientId: String, accessAsApplication: Boolean): String {
        val thisId = UUID.randomUUID().toString()

        val claims = mapOf(
            "oid" to thisId,
            "azp" to clientId,
            "roles" to if (accessAsApplication) listOf("access_as_application") else emptyList()
        )

        return mockOAuth2Server.issueToken(
            issuerId = "azuread",
            subject = thisId,
            audience = "aud-localhost",
            claims = claims
        ).serialize()
    }

    /**
     * On behalf
     * oid = unik id på brukeren i Azure AD
     * sub = unik id på brukeren i kombinasjon med applikasjon det ble logget inn i
     */
    fun onBehalfOfToken(mockOAuth2Server: MockOAuth2Server, role: String, saksbehandler: String): String {
        val clientId = UUID.randomUUID().toString()
        val brukerId = UUID.randomUUID().toString()

        val claims = mapOf(
            "oid" to brukerId,
            "azp" to clientId,
            "name" to saksbehandler,
            "NAVident" to saksbehandler,
            "groups" to listOf(role)
        )

        return mockOAuth2Server.issueToken(
            issuerId = "azuread",
            subject = UUID.randomUUID().toString(),
            audience = "aud-localhost",
            claims = claims
        ).serialize()
    }
}
