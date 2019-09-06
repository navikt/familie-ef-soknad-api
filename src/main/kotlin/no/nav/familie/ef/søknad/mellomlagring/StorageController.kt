package no.nav.familie.ef.søknad.mellomlagring

import no.nav.familie.ef.søknad.util.TokenUtil
import no.nav.security.oidc.api.ProtectedWithClaims
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@ProtectedWithClaims(issuer = TokenUtil.ISSUER, claimMap = ["acr=Level4"])
@RequestMapping(StorageController.REST_STORAGE)
class StorageController(private val storageService: StorageService) {

    @GetMapping
    fun hentSøknad(): ResponseEntity<String> {
        val muligKSøknad = storageService.hentSøknad()
        return if (muligKSøknad != null) ResponseEntity.ok().body(muligKSøknad)
        else ResponseEntity.noContent().build()
    }

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    fun lagreSøknad(@RequestBody søknad: String): ResponseEntity<String> {
        storageService.lagreSøknad(søknad)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping
    fun slettSøknad(): ResponseEntity<String> {
        storageService.slettSøknad()
        return ResponseEntity.noContent().build()
    }

    @GetMapping("vedlegg/{key}")
    fun getAttachment(@PathVariable("key") key: String): ResponseEntity<ByteArray> {
        val muligVedlegg = storageService.hentVedlegg(key)
        return muligVedlegg?.asOKHTTPEntity() ?: ResponseEntity.notFound().build()
    }

    @PostMapping(path = ["/vedlegg"], consumes = [MULTIPART_FORM_DATA_VALUE])
    fun storeAttachment(@RequestPart("vedlegg") attachmentMultipartFile: MultipartFile): ResponseEntity<String> {
        val vedlegg = Vedlegg.of(attachmentMultipartFile)
        storageService.lagreVedlegg(vedlegg)
        return ResponseEntity.created(vedlegg.uri()).body(vedlegg.uuid)
    }

    @DeleteMapping("vedlegg/{key}")
    fun deleteAttachment(@PathVariable("key") key: String): ResponseEntity<String> {
        storageService.slettVedlegg(key)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("kvittering/{type}")
    fun getKvittering(@PathVariable("type") type: String): ResponseEntity<String> {
        val muligKvittering = storageService.hentKvittering(type)
        return if (muligKvittering != null) ResponseEntity.ok().body(muligKvittering)
        else ResponseEntity.noContent().build()
    }

    @PostMapping(value = ["kvittering/{type}"], consumes = [APPLICATION_JSON_VALUE])
    fun storeKvittering(@PathVariable("type") type: String, @RequestBody kvittering: String): ResponseEntity<String> {
        storageService.lagreKvittering(type, kvittering)
        return ResponseEntity.noContent().build()
    }

    companion object {
        const val REST_STORAGE = "/rest/storage"
    }

}
