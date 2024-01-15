package no.nav.familie.ef.søknad.modeltest

import no.nav.familie.ef.søknad.søknad.domain.Adresse
import no.nav.familie.ef.søknad.søknad.domain.Aksjeselskap
import no.nav.familie.ef.søknad.søknad.domain.Aktivitet
import no.nav.familie.ef.søknad.søknad.domain.AnnenForelder
import no.nav.familie.ef.søknad.søknad.domain.Arbeidsgiver
import no.nav.familie.ef.søknad.søknad.domain.Arbeidssøker
import no.nav.familie.ef.søknad.søknad.domain.Barn
import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.Bosituasjon
import no.nav.familie.ef.søknad.søknad.domain.DatoFelt
import no.nav.familie.ef.søknad.søknad.domain.DokumentFelt
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator
import no.nav.familie.ef.søknad.søknad.domain.Firma
import no.nav.familie.ef.søknad.søknad.domain.ListFelt
import no.nav.familie.ef.søknad.søknad.domain.Medlemskap
import no.nav.familie.ef.søknad.søknad.domain.PeriodeFelt
import no.nav.familie.ef.søknad.søknad.domain.PerioderBoddIUtlandet
import no.nav.familie.ef.søknad.søknad.domain.Person
import no.nav.familie.ef.søknad.søknad.domain.SamboerDetaljer
import no.nav.familie.ef.søknad.søknad.domain.Situasjon
import no.nav.familie.ef.søknad.søknad.domain.Sivilstatus
import no.nav.familie.ef.søknad.søknad.domain.Søker
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.søknad.domain.TidligereUtdanning
import no.nav.familie.ef.søknad.søknad.domain.UnderUtdanning
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

@Disabled
internal class BackendVsFrontendTest {

    val exportInterfaceRegex = Regex("export interface (.*) \\{\\n( {2}[\\S ]+\\n)*")
    val fieldRegex = Regex(" {2}([a-zA-ZÅÆØåæø0-9]+)(\\?)?: (\\S+);")

    val exportEnumRegex = Regex("export enum (.*) \\{\\n( {2}[\\S ]+\\n)*")
    val enumFieldRegex = Regex(" {2}(\\S+) = '(\\S+)'")

    val ignoredInterfaces = setOf(
        "IHjelpetekst",
        "Toggles",
        "ISpørsmålFelt extends ITekstFelt",
        "ISpørsmålBooleanFelt extends IBooleanFelt",
        "ISpørsmålListeFelt extends ITekstListeFelt",
        "ILabel",
        "ISpørsmål extends ILabel",
        "ISvar",
        "ITekst",
        "LocaleString",
        "Språk",
        "IMellomlagretOvergangsstønad",
        "IDokumentasjon", // ?
    )

    val interfaceMappings: Map<String, KClass<out Any>> = mapOf(
        "IBarn" to Barn::class,
        "IForelder" to AnnenForelder::class,
        "IAdresse" to Adresse::class,
        "IPersonDetaljer" to SamboerDetaljer::class,
        "IPeriode" to PeriodeFelt::class,
        // "IHjelpetekst" to Hjelpe,
        "ISøknad" to SøknadOvergangsstønadDto::class,
        "IPerson" to Person::class,
        "ISøker" to Søker::class,
        // "IDokumentasjon" to
        "IDinSituasjon" to Situasjon::class,
        "IFirma" to Firma::class,
        "IUtdanning" to TidligereUtdanning::class,
        "IUnderUtdanning extends IUtdanning" to UnderUtdanning::class,
        "IArbeidsgiver" to Arbeidsgiver::class,
        "IAksjeselskap" to Aksjeselskap::class,
        "IArbeidssøker" to Arbeidssøker::class,
        "IAktivitet" to Aktivitet::class,
        "IBosituasjon" to Bosituasjon::class,
        "ISivilstatus" to Sivilstatus::class,
        "IMedlemskap" to Medlemskap::class,
        "IUtenlandsopphold" to PerioderBoddIUtlandet::class,
        "IVedlegg" to DokumentFelt::class,

        "ITekstFelt" to TekstFelt::class,
        "IBooleanFelt" to BooleanFelt::class,
        "IDatoFelt" to DatoFelt::class,
        "ITekstListeFelt" to ListFelt::class,
    )

    fun listFiles(dir: String): List<Path> = Files.list(Paths.get(dir))
        .toList()
        .flatMap {
            if (it.toFile().isFile) {
                listOf(it.toAbsolutePath())
            } else {
                listFiles(it.toString())
            }
        }

    @Test
    internal fun `check DokumentIdentifikator vs frontend`() {
        val file = Paths.get("../familie-ef-soknad/src/models/dokumentasjon.ts").toAbsolutePath()
        checkEnums(file, DokumentIdentifikator::class).print()
    }

    @Test
    internal fun `check data classes vs interfaces`() {
        val filePaths = listFiles("../familie-ef-soknad/src/models/")

        val medNullCheck = false
        println("ignored: ${ignoredInterfaces.joinToString(",")}\n")
        filePaths.flatMap { filePath -> handleFiles(filePath, interfaceMappings, medNullCheck) }.print()
    }

    enum class ResultType {
        OK,
        SAVNER_FELT,
        INGEN_MAPPING,
    }

    data class Result(val name: String, val type: ResultType, val str: List<String> = emptyList())

    fun List<Result>.print() {
        this.groupBy { it.type }
            .toSortedMap()
            .forEach { key, value ->

                value.forEach { v ->
                    println("${v.name} - ${v.type}")
                    v.str.forEach { println(it) }
                    if (key != ResultType.OK) {
                        println()
                    }
                }
                println("-----\n")
            }
    }

    private fun checkEnums(filePath: Path, enum: KClass<out Enum<*>>): List<Result> {
        val fileContent = Files.readString(filePath)
        val jsValues = exportEnumRegex.findAll(fileContent).map { match ->
            val jsFieldsString = match.groups[0]!!.value

            enumFieldRegex.findAll(jsFieldsString)
                .map {
                    val value = it.groups[1]!!.value
                    assertThat(value).isEqualTo(it.groups[2]!!.value)
                    value
                }
                .toSet()
        }.flatten().toSet()

        val enumValues = enum.java.enumConstants.map { it.name }.toSet()

        return listOf(
            Result(
                "jsValues",
                ResultType.SAVNER_FELT,
                jsValues.filterNot { it in enumValues }
                    .toList(),
            ),
            Result(
                "enumValuesValues",
                ResultType.SAVNER_FELT,
                enumValues.filterNot { it in jsValues }
                    .toList(),
            ),
        )
    }

    private fun handleFiles(
        filePath: Path,
        interfaceMappings: Map<String, KClass<out Any>>,
        medNullCheck: Boolean = true,
    ): List<Result> {
        val fileContent = Files.readString(filePath)

        val exportFinds = exportInterfaceRegex.findAll(fileContent)
            .filterNot { ignoredInterfaces.contains(it.groups[1]!!.value) }
        return exportFinds.map { match ->
            val interfaceName = match.groups[1]!!.value
            val jsFieldsString = match.groups[0]!!.value
            val mappingClass = this.interfaceMappings[interfaceName]
            if (mappingClass != null) {
                val jsFields = fieldRegex.findAll(jsFieldsString)
                    .map { it.groups[1]!!.value + if (medNullCheck) (it.groups[2]?.value ?: "") else "" }
                    .toSet()

                val fields = mappingClass.declaredMemberProperties
                val fieldnames = fields.map { it.name + if (medNullCheck && it.returnType.isMarkedNullable) "?" else "" }.toSet()

                val jsFieldsDiff = jsFields.toMutableSet()
                val classFieldsDiff = fieldnames.toMutableSet()
                jsFieldsDiff.removeAll(fieldnames)
                classFieldsDiff.removeAll(jsFields)
                jsFieldsDiff.remove("react_key")
                jsFieldsDiff.remove("id")
                classFieldsDiff.remove("react_key")

                if (jsFieldsDiff.isEmpty() && classFieldsDiff.isEmpty()) {
                    return@map Result(
                        interfaceName,
                        ResultType.OK,
                    )
                } else {
                    val tekster = jsFieldsDiff.map { " jsFelt $it" } + classFieldsDiff.map { " kotlin felt $it" }
                    return@map Result(
                        interfaceName,
                        ResultType.SAVNER_FELT,
                        tekster,
                    )
                }
            } else {
                return@map Result(
                    interfaceName,
                    ResultType.INGEN_MAPPING,
                    listOf("~~Finner ikke mapping for $interfaceName"),
                )
            }
        }.toList()
    }
}
