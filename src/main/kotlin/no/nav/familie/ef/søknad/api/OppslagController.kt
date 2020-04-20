package no.nav.familie.ef.søknad.api


import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.service.OppslagService
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.familie.ef.søknad.validering.SjekkGyldigPostnummer
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [OppslagController.OPPSLAG], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
@Validated
class OppslagController(private val oppslagService: OppslagService) {

    @GetMapping("/sokerinfo")
    fun søkerinfo(): Søkerinfo {
        return oppslagService.hentSøkerinfo()
    }

    @GetMapping("/poststed/{postnummer}")
    fun postnummer(@SjekkGyldigPostnummer @PathVariable postnummer: String): ResponseEntity<String> {
        val poststed = oppslagService.hentPoststedFor(postnummer)
        return if (!poststed.isNullOrBlank()) ResponseEntity.ok().body(poststed)
        else ResponseEntity.noContent().build()
    }

    companion object {
        const val OPPSLAG = "/api/oppslag"
    }
}