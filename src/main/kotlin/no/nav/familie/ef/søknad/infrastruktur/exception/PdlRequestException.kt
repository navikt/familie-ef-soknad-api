package no.nav.familie.ef.søknad.infrastruktur.exception

open class PdlRequestException(melding: String? = null) : Exception(melding)
class PdlNotFoundException : PdlRequestException()
