package no.nav.familie.ef.søknad.validering

import org.springframework.stereotype.Component
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Component
class PostnummerValidator() : ConstraintValidator<SjekkGyldigPostnummer, String> {

    override fun isValid(postnummer: String, ctx: ConstraintValidatorContext): Boolean {
        if (postnummer.length === 4 && !postnummer.contains("+")) {
            try {
                val postNummerInt = postnummer.toInt()
                if (postNummerInt > 0) {
                    return true
                }
            } catch (e: NumberFormatException) {
                // do nothing
            }
        }
        return false
    }

}