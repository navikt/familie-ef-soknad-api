package no.nav.familie.ef.søknad.api


import no.nav.familie.ef.søknad.service.Dokument
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = [DokumentController.DOKUMENT], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
//@Unprotected
class DokumentController(private val dokument: Dokument) {

    @GetMapping("/hent/{testid}")
    fun hentVedlegg(@PathVariable testid: String): ResponseEntity<String> {
        val vedlegg = dokument.hentVedlegg(testid)
        return if (!vedlegg.isNullOrBlank()) ResponseEntity.ok().body(vedlegg)
        else ResponseEntity.noContent().build()
    }

    @GetMapping
    fun test(): ResponseEntity<String> {
        val vedlegg = dokument.test()
        return if (!vedlegg.isNullOrBlank()) ResponseEntity.ok().body(vedlegg)
        else ResponseEntity.noContent().build()
    }

    companion object {
        const val DOKUMENT = "/api/dokument"
    }
}