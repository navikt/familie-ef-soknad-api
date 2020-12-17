package no.nav.familie.ef.søknad.mapper

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.familie.kontrakter.felles.objectMapper
import org.slf4j.LoggerFactory

val kontekst = ThreadLocal<Språk>()

fun String.hentTekst(): String = Oversetter.hentTekst(this)
fun språk(): Språk = kontekst.get() ?: Språk.NB

enum class Språk(val språk: String) {
    @JsonProperty("nb")
    NB("nb"),

    @JsonProperty("en")
    EN("en")

    ;

    companion object {

        private val map = values().associateBy(Språk::språk)
        fun fromString(type: String) = map[type]
    }
}

object Oversetter {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val tekst: String = this::class.java.classLoader.getResource("tekst/tekst.json").readText()
    private val tekstMap: Map<Språk, Map<String, String>> = objectMapper.readValue(tekst)

    fun hentTekst(key: String): String {
        return tekstMap[språk()]?.get(key) ?: run {
            logger.warn("Fant ikke oversettelse for: $key, ${språk()}")
            return key
        }
    }
}

