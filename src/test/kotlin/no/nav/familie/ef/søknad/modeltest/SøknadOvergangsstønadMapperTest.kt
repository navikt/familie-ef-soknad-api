package no.nav.familie.ef.søknad.modeltest

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.mockk.spyk
import io.mockk.verify
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DatoFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.ListFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadOvergangsstønadMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.kontrakter.ef.søknad.Dokumentasjon
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor

data class Foo(
    val a: Søknadsfelt<Boolean>,
    val b: List<Søknadsfelt<String>>,
    val c: Søknadsfelt<List<String>>,
    val d: Søknadsfelt<Bar>? = null,
)

data class Bar(val aa: Søknadsfelt<String>? = null)

data class Zoo(val bb: Søknadsfelt<Bar>)
data class Zoo2(val b: List<TekstFelt>)

@Disabled
internal class SøknadOvergangsstønadMapperTest {

    @BeforeEach
    internal fun setUp() {
        val logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        logger.level = Level.INFO
    }

    private val søknadMapper = SøknadOvergangsstønadMapper()

    @Test
    internal fun checkAndPrintErrors() {
        val søknadDto = søknadDto()
        val søknad = søknadMapper.mapTilIntern(søknadDto, LocalDateTime.now())
        checkAndPrintErrors(søknad)
    }

    @Test
    internal fun checkAllMethodsCalled() {
        val createCopy = spyk(createCopy(søknadDto()).copy(dokumentasjonsbehov = emptyList()))
        søknadMapper.mapTilIntern(createCopy, LocalDateTime.now())
        println("\n------")
        println("Checking all methods called")
        checkAllMethodsCalled(createCopy)
    }

    private fun checkAllMethodsCalled(k: Any, depth: List<String> = emptyList()) {
        val declaredMemberProperties = k::class.declaredMemberProperties
        declaredMemberProperties.forEach {
            val newDepth = depth + k::class.simpleName!! + it.name
            if (it.name == "dokumentasjonsbehov") {
                return@forEach
            }
            val depthString = newDepth.joinToString(" -> ")
            try {
                verify(atLeast = 1) { it.call(k) }
            } catch (e: Throwable) {
                println("$depthString \n")
                return@forEach
            }
            if (it.returnType.classifier == ListFelt::class) {
                val listFelt = it.call(k) as ListFelt<*>
                try {
                    verify(atLeast = 1) { listFelt.label }
                } catch (e: Throwable) {
                    println("$depthString \n")
                }
                try {
                    verify(atLeast = 1) { listFelt.verdi }
                } catch (e: Throwable) {
                    println("$depthString \n")
                    // throw e
                }
            } else if ((it.returnType.classifier as KClass<*>).isSubclassOf(Collection::class)) {
                val collection = it.call(k) as Collection<*>
                checkAllMethodsCalled(
                    collection.firstOrNull()
                        ?: throw IllegalStateException("Finner ikke første element på liste: $depthString"),
                    newDepth,
                )
            } else if (!it.returnType.toString().startsWith("kotlin.") &&
                !it.returnType.toString().startsWith("java.")
                // && it.returnType.classifier != ListFelt::class
            ) {
                checkAllMethodsCalled(
                    it.call(k) ?: throw IllegalStateException("Finner ikke noe data på element:${it.name}"),
                    newDepth,
                )
            }
        }
    }

    private fun <T : Any> createCopy(any: T, depth: List<String> = emptyList()): T {
        val kClass = any::class
        if (any is String || any is LocalDate || any is Boolean || any is Int) {
            return any
        }
        val params = (
            konstruktørparametere(any)
                .map { finnSøknadsfelt(any, it) }
                .map {
                    val substringAfterLast = it.returnType.classifier.toString().substringAfterLast(".")
                    if (it.name == "fødselsdato" && it.returnType.classifier == TekstFelt::class) {
                        return@map spyk(TekstFelt("fødselsdato", "2010-01-01"))
                    }
                    val newDepth: List<String> = depth + substringAfterLast
                    val value = it.call(any)
                    if (value == null) {
                        println("Null ${newDepth.joinToString("->")} ${it.name}")
                        if (it.returnType.classifier == TekstFelt::class) {
                            return@map spyk(TekstFelt("label", "21057822284"))
                        }
                        if (it.returnType.classifier == BooleanFelt::class) {
                            return@map spyk(BooleanFelt("label", true))
                        }
                        if (it.returnType.classifier == DatoFelt::class) {
                            // return@map spyk(DatoFelt("label", LocalDate.now()))
                        }
                        throw RuntimeException("Error")
                    }
                    if (value is List<*>) {
                        return@map value.map { item ->
                            val createCopy = createCopy(item!!, newDepth)
                            if (createCopy::class.qualifiedName!!.startsWith("no.")) {
                                spyk(createCopy)
                            } else {
                                createCopy
                            }
                        }
                    }
                    if (value is String || value::class.qualifiedName!!.startsWith("kotlin.")) {
                        return@map value
                    }

                    try {
                        val objToCopy = createCopy(value, newDepth)
                        val spyk = spyk(objToCopy)
                        return@map spyk
                    } catch (e: Exception) {
                        println("Error ${newDepth.joinToString("->")} ${it.name}")
                        throw e
                    }
                }
            ).toTypedArray()
        try {
            val copyFunction = kClass.memberFunctions.first { it.name == "copy" }
            val call = copyFunction.call(any, *params)
            val t = call as T
            return t
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun finnSøknadsfelt(entity: Any, konstruktørparameter: KParameter) =
        entity::class.declaredMemberProperties.first { it.name == konstruktørparameter.name }

    private fun konstruktørparametere(entity: Any) = entity::class.primaryConstructor?.parameters ?: emptyList()

    private fun findErrors(any: Any, depth: List<String> = emptyList()): List<Error> {
        val anyClass = any::class
        val declaredMemberProperties = anyClass.declaredMemberProperties

        if (any is PersonMinimum) {
            return emptyList()
        }

        return declaredMemberProperties.flatMap {
            val newDepth: List<String> = depth + anyClass.simpleName!!
            val v = it.call(any)
            if (it.returnType.arguments.firstOrNull()?.type?.classifier == Dokumentasjon::class) {
                return emptyList()
            } else if (v == null) {
                listOf(Error(it.name, newDepth))
            } else {
                if (v is Søknadsfelt<*>) {
                    val verdi = v.verdi
                    if (verdi == null) {
                        listOf(
                            Error(
                                "${it.name} - verdi",
                                newDepth,
                            ),
                        )
                    } else {
                        if (isJavaClass(verdi)) {
                            // ignore
                            emptyList<Error>()
                        } else if (verdi is Collection<*>) {
                            println("isCollection")
                            emptyList()
                        } else {
                            findErrors(verdi, newDepth)
                        }
                    }
                } else if (v is Collection<*>) {
                    if (v.isEmpty()) {
                        listOf(
                            Error(
                                "${it.name} - collection empty",
                                newDepth,
                            ),
                        )
                    } else {
                        findErrors(v.first()!!, newDepth)
                    }
                } else if (isJavaClass(v)) {
                    // ignore
                    emptyList<Error>()
                } else {
                    println("notSøknadsfelt ${v::class.simpleName} (${newDepth.joinToString("->")})")
                    emptyList<Error>()
                }
            }
        }
    }

    private fun isJavaClass(verdi: Any): Boolean {
        val className = verdi::class.qualifiedName!!
        return className.startsWith("kotlin.") || className.startsWith("java.")
    }

    private fun checkAndPrintErrors(any: Any) {
        val errors = findErrors(any)
        println("----")
        println(any::class.simpleName)
        if (errors.isEmpty()) {
            println(" No errors")
        } else {
            errors.forEach { println(" ${it.depth.joinToString("->")}: ${it.name}") }
        }
    }

    private fun formatDepth(depth: List<String>) = depth.joinToString(" -> ")

    data class Error(val name: String, val depth: List<String>)
}
