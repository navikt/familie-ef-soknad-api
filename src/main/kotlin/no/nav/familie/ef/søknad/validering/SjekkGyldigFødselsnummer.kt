package no.nav.familie.ef.søknad.validering

import no.nav.familie.ef.sak.validering.FnrValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Suppress("unused")
@MustBeDocumented
@Constraint(validatedBy = [FnrValidator::class])
@Target( AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class SjekkGyldigFødselsnummer(val message: String = "Personident i søknad er ikke samme som innlogget bruker",
                                          val groups: Array<KClass<*>> = [],
                                          val payload: Array<KClass<out Payload>> = [])
