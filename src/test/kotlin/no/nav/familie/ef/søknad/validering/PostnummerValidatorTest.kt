package no.nav.familie.ef.søknad.validering

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.validation.ConstraintValidatorContext

internal class PostnummerValidatorTest {

    private val postnummerValidator: PostnummerValidator = PostnummerValidator()
    private val constraintValidatorContext: ConstraintValidatorContext = mockk()

    @Test
    fun `Gyldig postnummer skal returnere true `() {
        val valid = postnummerValidator.isValid("9700", constraintValidatorContext)
        assertThat(valid).isTrue()
    }

    @Test
    fun `Ugyldig (tekst) postnummer skal returnere false `() {
        val valid = postnummerValidator.isValid("tekst", constraintValidatorContext)
        assertThat(valid).isFalse()
    }

    @Test
    fun `Ugyldig (for lang) postnummer skal returnere false `() {
        val valid = postnummerValidator.isValid("12345", constraintValidatorContext)
        assertThat(valid).isFalse()
    }

    @Test
    fun `Ugyldig (negativ) postnummer skal returnere false `() {
        val valid = postnummerValidator.isValid("-123", constraintValidatorContext)
        assertThat(valid).isFalse()
    }

}
