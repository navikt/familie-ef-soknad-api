package no.nav.familie.ef.søknad.mapper

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.familie.kontrakter.felles.objectMapper
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import java.util.*

fun String.hentTekst(): String = Oversettelser.hentTekst(this)


object Oversettelser {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val tekst: Map<String, Map<String, String>> =
            objectMapper.readValue(javaClass.getResource("/tekst/tekst.json").readText())

    fun hentTekst(key: String): String {
        return tekst[hentSpråk()]?.get(key) ?: run {
            logger.warn("Fant ikke oversettelse for: $key, ${hentSpråk()}")
            return key
        }
    }


    private fun hentSpråk(): String {
        val locale: Locale = LocaleContextHolder.getLocale()
        return when (locale.language) {
            "nb" -> "nb"
            "en" -> "en"
            else -> run {
                logger.warn("Locale (${locale.language}) ikke gyldig, returnerer default (nb)")
                "nb"
            }

        }

    }

}

