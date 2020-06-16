package no.nav.familie.ef.søknad.mock

import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping(path = ["/dokumentmock/"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Unprotected
class DokumentController(private val dokumenter: MutableMap<String, ByteArray>) {

    @PostMapping(path = ["{bucket}"],
                 consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
                 produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addAttachment(@PathVariable("bucket") bucket: String,
                      @RequestParam("file") multipartFile: MultipartFile): ResponseEntity<Map<String, String>> {
        if (multipartFile.isEmpty) {
            return ResponseEntity.ok(emptyMap())
        }

        val bytes = multipartFile.bytes
        val maxFileSizeInBytes = MAX_FILE_SIZE_MB * 1024 * 1024

        if (bytes.size > maxFileSizeInBytes) {
            throw IllegalArgumentException(HttpStatus.PAYLOAD_TOO_LARGE.toString())
        }

        val uuid = UUID.randomUUID().toString()
        try {
            dokumenter[uuid] = bytes
        } catch (e: RuntimeException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

        return ResponseEntity.ok(mapOf("dokumentId" to uuid, "filnavn" to multipartFile.name))
    }

    companion object {
        const val MAX_FILE_SIZE_MB = 10
    }
}