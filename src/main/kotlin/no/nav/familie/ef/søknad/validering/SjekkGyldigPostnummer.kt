package no.nav.familie.ef.søknad.validering

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Suppress("unused")
@MustBeDocumented
@Constraint(validatedBy = [PostnummerValidator::class])
@Target( AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class SjekkGyldigPostnummer(val message: String = "Postnummer er ikke gyldig",
                                       val groups: Array<KClass<*>> = [],
                                       val payload: Array<KClass<out Payload>> = [])
