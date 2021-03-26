package no.nav.familie.ef.søknad.exception

//class PdlRequestException(melding: String) : Exception(melding)

open class PdlRequestException(melding: String? = null) : Exception(melding)
class PdlNotFoundException : PdlRequestException()
