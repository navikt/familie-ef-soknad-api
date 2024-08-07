package no.nav.familie.ef.søknad.person

import no.nav.familie.ef.søknad.kodeverk.KodeverkService
import no.nav.familie.ef.søknad.person.domain.Søkerinfo
import no.nav.familie.ef.søknad.person.dto.PersonMinimumDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
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
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"])
@Validated
class OppslagController(
    private val oppslagService: OppslagService,
    private val kodeverkService: KodeverkService,
) {
    @GetMapping("/sokerinfo")
    fun søkerinfo(): Søkerinfo = oppslagService.hentSøkerinfo()

    @GetMapping("/sokerminimum")
    fun søkerinfominimum(): PersonMinimumDto {
        val søkerNavn = oppslagService.hentSøkerNavn()
        return søkerNavn.tilSøkerMinimumDto()
    }

    @GetMapping("/poststed/{postnummer}")
    fun postnummer(
        @PathVariable postnummer: String,
    ): ResponseEntity<String> {
        require(gyldigPostnummer(postnummer))
        val poststed = kodeverkService.hentPoststed(postnummer)
        return if (!poststed.isNullOrBlank()) {
            ResponseEntity.ok().body(poststed)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    private fun gyldigPostnummer(postnummer: String) = Regex("""^[0-9]{4}$""").matches(postnummer)

    companion object {
        const val OPPSLAG = "/api/oppslag"
    }

    private fun String.tilSøkerMinimumDto(): PersonMinimumDto = PersonMinimumDto(EksternBrukerUtils.hentFnrFraToken(), this)
}
