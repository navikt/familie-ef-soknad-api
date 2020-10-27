package no.nav.familie.ef.søknad.api


import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.service.KodeverkService
import no.nav.familie.ef.søknad.service.OppslagService
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [OppslagController.OPPSLAG], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER, claimMap = ["acr=Level4"])
@Validated
class OppslagController(private val oppslagService: OppslagService,
                        private val kodeverkService: KodeverkService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/sokerinfo")
    fun søkerinfo(): Søkerinfo {
        return oppslagService.hentSøkerinfo()
    }

    @GetMapping("/sokerinfoV2")
    fun søkerinfo_V2(): Søkerinfo {
        return oppslagService.hentSøkerinfoV2()
    }

    @GetMapping("/poststed/{postnummer}")
    fun postnummer(@PathVariable postnummer: String): ResponseEntity<String> {
        require(gyldigPostnummer(postnummer))
        val poststed = kodeverkService.hentPoststed(postnummer)
        return if (!poststed.isNullOrBlank()) ResponseEntity.ok().body(poststed)
        else ResponseEntity.noContent().build()
    }

    private fun gyldigPostnummer(postnummer: String) = Regex("""^[0-9]{4}$""").matches(postnummer)

    companion object {
        const val OPPSLAG = "/api/oppslag"
    }
}