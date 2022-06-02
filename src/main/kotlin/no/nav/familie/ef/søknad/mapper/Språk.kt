package no.nav.familie.ef.søknad.mapper

import com.fasterxml.jackson.annotation.JsonProperty

val kontekst = ThreadLocal<Språk>()

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
