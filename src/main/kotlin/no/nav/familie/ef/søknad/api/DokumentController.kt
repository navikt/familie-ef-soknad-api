package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.service.Dokument
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = [DokumentController.DOKUMENT], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
class DokumentController(private val dokument: Dokument) {

    @GetMapping("/hent/{vedleggsId}")
    fun hentVedlegg(@PathVariable vedleggsId: String): ResponseEntity<ByteArray> {
        val vedlegg = dokument.hentVedlegg(vedleggsId)
        return if (vedlegg != null) ResponseEntity.ok(vedlegg)
        else ResponseEntity.noContent().build()
    }

    companion object {
        const val DOKUMENT = "/api/dokument"
    }
}