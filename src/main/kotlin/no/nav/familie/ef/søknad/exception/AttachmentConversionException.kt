package no.nav.familie.ef.søknad.exception

import org.springframework.http.MediaType

class AttachmentConversionException(msg: String, e: Throwable, mediaType: MediaType? = null)
    : AttachmentException(mediaType, e, msg)