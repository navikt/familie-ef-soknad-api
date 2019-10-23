package no.nav.familie.ef.søknad.util


object InnloggingUtils {

    private const val selvbetjening = "selvbetjening"



    fun hentFnrFraToken(): String {
        return TokenUtil().fødselsnummer
    }

    fun generateBearerTokenForLoggedInUser(): String {
        return TokenUtil().getBearerTokenForLoggedInUser()

    }

}
