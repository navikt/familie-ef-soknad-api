package no.nav.familie.ef.søknad.integrationTest


import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import java.util.UUID

object TokenUtil {


    //    fun getAuthCookie(
//            fnr: String,
//            level: Int = 4,
//            cookieName: String = "localhost-idtoken",
//            expiry: Long? = null) : Cookie {
//
//        val overridingClaims : Map<String, Any> = if (expiry == null) emptyMap() else mapOf(
//                "exp" to expiry
//        )
//
//        val jwt = LoginService.V1_0.generateJwt(fnr = fnr, level = level, overridingClaims = overridingClaims)
//        return Cookie(listOf(String.format("%s=%s", cookieName, jwt), "Path=/", "Domain=localhost"))
//    }


    fun søkerToken(mockOAuth2Server: MockOAuth2Server): String {








    return mockOAuth2Server.issueToken(
            issuerId = EksternBrukerUtils.ISSUER,
            subject = UUID.randomUUID().toString(),
            audience = "0090b6e1-ffcc-4c37-bc21-049f7d1f0fe5",
    ).serialize()

    }




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
    fun onBehalfOfToken(mockOAuth2Server: MockOAuth2Server, role: String, søker: String): String {
        val clientId = UUID.randomUUID().toString()
        val brukerId = UUID.randomUUID().toString()

        val claims = mapOf(
                "oid" to brukerId,
                "azp" to clientId,
                "name" to søker,
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