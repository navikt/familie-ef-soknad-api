package no.nav.familie.ef.søknad.exception

open class PdlRequestException(melding: String? = null) : Exception(melding)
class PdlNotFoundException : PdlRequestException()
