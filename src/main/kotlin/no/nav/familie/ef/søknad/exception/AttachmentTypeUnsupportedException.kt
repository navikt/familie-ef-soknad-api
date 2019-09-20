package no.nav.familie.ef.søknad.exception

import org.springframework.http.MediaType

class AttachmentTypeUnsupportedException(mediaType: MediaType?,
                                         msg: String? = "Media type $mediaType er ikke støttet",
                                         e: Throwable? = null)
    : AttachmentException(mediaType, e, msg)