package no.nav.familie.ef.søknad.mock

import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.security.token.support.core.api.Unprotected
import no.nav.security.token.support.core.context.TokenValidationContextHolder
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Profile("mock-dokument")
@Suppress("SpringJavaInjectionPointsAutowiringInspection") // dokumentlager er definiert i DokumentConfiguration
@RestController
@RequestMapping(path = ["/dokumentmock/"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Unprotected
class DokumentMockController(
    @Qualifier("dokumentlager") private val dokumenter: MutableMap<String, ByteArray>,
    private val contextHolder: TokenValidationContextHolder,
) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    private val mellomlager = mutableMapOf<String, String>()

    fun TokenValidationContextHolder.hentFnr(): String {
        return this.getTokenValidationContext().getJwtToken("tokenx")?.subject ?: throw JwtTokenValidatorException("Klarte ikke hente fnr tokenx-token")
    }

    @PostMapping(
        path = ["dokument/{bucket}"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun addAttachment(
        @PathVariable("bucket") bucket: String,
        @RequestParam("file") multipartFile: MultipartFile,
    ): ResponseEntity<Map<String, String>> {
        if (multipartFile.isEmpty) {
            return ResponseEntity.ok(emptyMap())
        }

        val bytes = multipartFile.bytes
        val maxFileSizeInBytes = MAX_FILE_SIZE_MB * 1024 * 1024

        require(bytes.size > maxFileSizeInBytes) { HttpStatus.PAYLOAD_TOO_LARGE.toString() }

        return try {
            val uuid = UUID.randomUUID().toString()
            dokumenter[uuid] = bytes
            ResponseEntity.ok(mapOf("dokumentId" to uuid, "filnavn" to multipartFile.name))
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping(
        "mellomlager",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun mellomlagreSøknad(@RequestBody(required = true) søknad: String): ResponseEntity<Unit> {
        log.info("Mellomlagrer søknad om overgangsstønad")

        validerGyldigJson(søknad)
        val directory = contextHolder.hentFnr()

        try {
            mellomlager[directory] = søknad
        } catch (e: RuntimeException) {
            log.warn("Kunne ikke mellomlagre overgangsstønad for $directory", e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("mellomlager", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hentMellomlagretSøknad(): ResponseEntity<String> {
        val directory = contextHolder.hentFnr()
        return try {
            ResponseEntity.ok(mellomlager[directory])
        } catch (e: RuntimeException) {
            log.info("Noe gikk galt", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @DeleteMapping("mellomlager", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun slettMellomlagretSøknad(): ResponseEntity<String> {
        val directory = contextHolder.hentFnr()
        return try {
            log.debug("Sletter mellomlagret overgangsstønad")
            mellomlager.remove(directory)
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } catch (e: RuntimeException) {
            log.warn("Kunne ikke slette mellomlagret overgangsstønad for $directory", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    private fun validerGyldigJson(verdi: String) {
        try {
            objectMapper.readTree(verdi)
        } catch (e: Exception) {
            error("Forsøker å mellomlagre søknad som ikke er gyldig json-verdi")
        }
    }

    companion object {
        const val MAX_FILE_SIZE_MB = 20
    }
}
