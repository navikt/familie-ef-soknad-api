package no.nav.familie.ef.søknad.api


import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.service.Oppslag
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.security.token.support.core.api.Unprotected
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [OppslagController.OPPSLAG], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
class OppslagController(private val oppslag: Oppslag) {

    @GetMapping("/sokerinfo")
    fun søkerinfo(): Søkerinfo {
        return oppslag.hentSøkerinfo()
    }

    @Unprotected
    @GetMapping("/poststed/{postnummer}")
    fun postnummer(@PathVariable postnummer: String): ResponseEntity<String> {
        val poststed = oppslag.hentPoststedFor(postnummer)
        return if (poststed != "") ResponseEntity.ok().body(poststed)
        else ResponseEntity.noContent().build()
    }

    companion object {
        const val OPPSLAG = "/api/oppslag"
    }
}