package no.nav.familie.ef.søknad.validering

import no.nav.familie.ef.søknad.validering.SjekkGyldigFødselsnummer
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.springframework.stereotype.Component
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Component
class FnrValidator() : ConstraintValidator<SjekkGyldigFødselsnummer, String> {

    override fun isValid(fnr: String, ctx: ConstraintValidatorContext): Boolean {
        val hentFnrFraToken = EksternBrukerUtils.hentFnrFraToken()
        return hentFnrFraToken == fnr
    }

}
