package no.nav.familie.ef.søknad.service.mellomlagring

import com.google.gson.Gson
import no.nav.familie.ef.søknad.excpetion.AttachmentsTooLargeException
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.net.URI
import java.util.*

class Vedlegg private constructor(val filename: String,
                                  val bytes: ByteArray,
                                  private val contentType: MediaType,
                                  private val size: Long) {

    val uuid: String = UUID.randomUUID().toString()

    fun toJson(): String {
        return Gson().toJson(this)
    }

    fun uri(): URI {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uuid}")
                .buildAndExpand(this.uuid).toUri()
    }

    fun asOKHTTPEntity(): ResponseEntity<ByteArray> {
        return ResponseEntity.ok()
                .contentType(contentType)
                .contentLength(size)
                .body(bytes)
    }

    companion object {

        private const val MB = 1024L * 1024L
        private const val MAX_VEDLEGG_SIZE = 8 * MB

        fun of(file: MultipartFile): Vedlegg {
            val fileSize = file.size
            if (fileSize > MAX_VEDLEGG_SIZE) {
                throw AttachmentsTooLargeException(fileSize, MAX_VEDLEGG_SIZE)
            }
            return Vedlegg(file.originalFilename ?: "Unknown",
                           getBytes(file),
                           MediaType.valueOf(file.contentType!!),
                           fileSize)
        }

        private fun getBytes(file: MultipartFile): ByteArray {
            try {
                return file.bytes
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }

        fun fromJson(json: String): Vedlegg {
            return Gson().fromJson(json, Vedlegg::class.java)
        }
    }
}
