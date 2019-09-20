package no.nav.familie.ef.søknad.exception

import org.springframework.http.MediaType

abstract class AttachmentException constructor(val mediaType: MediaType? = null,
                                               e: Throwable? = null,
                                               msg: String? = null) : RuntimeException(msg, e)
