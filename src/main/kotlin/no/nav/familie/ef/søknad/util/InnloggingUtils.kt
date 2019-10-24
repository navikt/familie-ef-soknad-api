package no.nav.familie.ef.søknad.util


object InnloggingUtils {

    private const val ISSUER = "selvbetjening"



    fun hentFnrFraToken(): String {
        return TokenUtil().fødselsnummer
    }

    fun generateBearerTokenForLoggedInUser(): String {
        val jwtToken = TokenUtil().getTokenValidationContext().getJwtToken(ISSUER)
        val tokenAsString = jwtToken.tokenAsString
        return "Bearer $tokenAsString"
    }

}
