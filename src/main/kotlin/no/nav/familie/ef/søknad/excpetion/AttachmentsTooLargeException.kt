package no.nav.familie.ef.søknad.excpetion

import org.apache.commons.io.FileUtils.byteCountToDisplaySize
import java.lang.String.format

class AttachmentsTooLargeException(size: Long, max: Long) : AttachmentException(msg = msg(size, max)) {

    companion object {

        private fun msg(attachmentSize: Long, max: Long): String {
            return format("Vedlegg-størrelse er %s, men kan ikke overstige %s",
                          byteCountToDisplaySize(attachmentSize),
                          byteCountToDisplaySize(max))
        }
    }
}
