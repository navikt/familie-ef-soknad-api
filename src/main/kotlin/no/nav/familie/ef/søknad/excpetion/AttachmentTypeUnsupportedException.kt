package no.nav.familie.ef.søknad.excpetion

import org.springframework.http.MediaType

class AttachmentTypeUnsupportedException(mediaType: MediaType?,
                                         msg: String? = "Media type $mediaType er ikke støttet",
                                         e: Throwable? = null)
    : AttachmentException(mediaType, e, msg)