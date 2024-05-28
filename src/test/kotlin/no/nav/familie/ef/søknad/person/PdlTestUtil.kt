package no.nav.familie.ef.søknad.person

import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

object PdlTestUtil {
    fun parseSpørring(filnavn: String): Map<String, *> {
        val query = this::class.java.getResource(filnavn).readText()
        val readLines = query.reader().readLines()
        return toMap(readLines.listIterator())
    }

    fun finnFeltStruktur(entitet: Any?): Map<String, *>? {
        if (entitet == null) {
            return null
        }
        // Det går ike å hente elementene i en liste med reflection, så vi traverserer den som vanlig.
        if (entitet is List<*>) {
            return finnFeltStruktur(
                entitet.first(),
            )
        }

        val map =
            konstruktørparametere(
                entitet,
            )
                .map {
                    val annotation = it.annotations.firstOrNull()
                    val annotationClass = annotation?.annotationClass
                    val annotationValue =
                        annotationClass?.declaredMemberProperties
                            ?.firstOrNull { kProperty1 -> kProperty1.name == "value" }
                            ?.getter
                            ?.call(annotation) as String?
                    (annotationValue ?: it.name) to
                        finnSøknadsfelt(
                            entitet,
                            it,
                        )
                }
                .associateBy({ it.first!! }, {
                    finnFeltStruktur(
                        getFeltverdi(
                            it.second,
                            entitet,
                        ),
                    )
                })

        return if (map.isEmpty()) null else map
    }

    private fun toMap(stringLines: ListIterator<String>): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        stringLines.forEach {
            when {
                it.trim().endsWith("{") -> {
                    map[
                        parseToLabel(
                            it,
                        ),
                    ] =
                        toMap(
                            stringLines,
                        )
                }
                it.trim().endsWith("}") -> {
                    return map
                }
                else -> {
                    map[
                        parseToLabel(
                            it,
                        ),
                    ] = null
                }
            }
        }
        return map
    }

    private fun parseToLabel(line: String): String {
        if (line.trim().startsWith("query")) {
            return "data"
        }
        if (line.trim().startsWith("person: hentPerson")) {
            return "person"
        }
        if (line.trim().startsWith("personBolk: hentPersonBolk")) {
            return "personBolk"
        }
        return line.trim().substringBefore("{").substringBefore("(").trim()
    }

    /**
     * Henter ut verdien for felt på entitet.
     */
    private fun getFeltverdi(
        felt: KProperty1<out Any, Any?>,
        entitet: Any,
    ) = felt.getter.call(entitet)

    /**
     * Finn første (og eneste) felt på entiteten som har samme navn som konstruktørparameter.
     */
    private fun finnSøknadsfelt(
        entity: Any,
        konstruktørparameter: KParameter,
    ) = entity::class.declaredMemberProperties.first { it.name == konstruktørparameter.name }

    /**
     * Konstruktørparametere er det eneste som gir oss en garantert rekkefølge for feltene, så vi henter disse først.
     */
    private fun konstruktørparametere(entity: Any) = entity::class.primaryConstructor?.parameters ?: emptyList()
}
