package no.nav.familie.ef.sak.validering

import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.familie.ef.søknad.validering.SjekkGyldigFødselsnummer
import org.springframework.stereotype.Component
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Component
class FnrValidator() : ConstraintValidator<SjekkGyldigFødselsnummer, String> {

    override fun isValid(fnr: String, ctx: ConstraintValidatorContext): Boolean {
        val hentFnrFraToken = InnloggingUtils.hentFnrFraToken()
        return hentFnrFraToken == fnr
    }

}
