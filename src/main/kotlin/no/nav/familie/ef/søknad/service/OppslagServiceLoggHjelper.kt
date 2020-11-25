package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.tps.Barn
import no.nav.familie.ef.søknad.api.dto.tps.Person
import org.slf4j.LoggerFactory
import kotlin.reflect.full.memberProperties

@Deprecated("Denne skal fjernes så snart vi er helt over på PDL")
object OppslagServiceLoggHjelper {


    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun logDiff(søkerinfoV1: Søkerinfo, søkerinfoV2: Søkerinfo): String {
        val builder = StringBuilder()
        leggTilSøkerDiff(søkerinfoV1, søkerinfoV2, builder)
        leggTilBarneDiff(søkerinfoV1, søkerinfoV2, builder)
        if (builder.isNotBlank()) {
            secureLogger.warn(builder.toString())
        }
        return builder.toString()
    }

    private fun leggTilBarneDiff(søkerinfoV1: Søkerinfo,
                                 søkerinfoV2: Søkerinfo,
                                 builder: StringBuilder) {
        if (søkerinfoV1.barn.size != søkerinfoV2.barn.size) {
            builder.append("Ikke samme antall barn fra tps og pdl: \n V1: ${søkerinfoV1.barn}, \n V2: ${søkerinfoV2.barn}")
        } else {
            val barn1Liste = søkerinfoV1.barn.sortedBy { it.fnr }
            val barn2Liste = søkerinfoV2.barn.sortedBy { it.fnr }
            val zip = barn1Liste.zip(barn2Liste)
            zip.forEach {
                for (prop in Barn::class.memberProperties) {
                    val detErDiff = when (prop.name) {
                        "navn" -> {
                            listOfSeparatedNames(it.first.navn) != listOfSeparatedNames(it.second.navn)
                        }
                        else -> prop.get(it.first) != prop.get(it.second)
                    }
                    if (detErDiff) {
                        builder.append("\n Barn: ${prop.name} = V1: ${prop.get(it.first)}, V2: ${prop.get(it.second)}")
                    }
                }
            }
        }
    }

    private fun listOfSeparatedNames(name: String) =
            name.split(" ").sorted()

    private fun leggTilSøkerDiff(søkerinfoV1: Søkerinfo,
                                 søkerinfoV2: Søkerinfo,
                                 builder: StringBuilder) {
        for (prop in Person::class.memberProperties) {
            val property1 = prop.get(søkerinfoV1.søker)
            val property2 = prop.get(søkerinfoV2.søker)
            when (prop.name) {
                "sivilstand" -> {
                    logSivilstandsDiff(builder, søkerinfoV1.søker.sivilstand, søkerinfoV2.søker.sivilstand)
                }
                else -> {
                    if (property1 != property2 && prop.name != "sivilstand") {
                        builder.append("\n Person: ${prop.name} = V1: $property1, V2: $property2")
                    }
                }
            }
        }
    }

    private fun logSivilstandsDiff(builder: StringBuilder, sivilstand1: String, sivilstand2: String) {

        val erLikSivilstand = !when (sivilstand1) {
            "SEPA", "SEPARERT" -> {
                listOf("SEPA", "SEPARERT").contains(sivilstand2)
            }
            "UGIF", "UGIFT" -> {
                listOf("UGIF", "UGIFT").contains(sivilstand2)
            }
            "SKIL", "SKILT" -> {
                listOf("SKIL", "SKILT").contains(sivilstand2)
            }
            "ENKE", "ENKE_ELLER_ENKEMANN" -> {
                listOf("ENKE", "ENKE_ELLER_ENKEMANN").contains(sivilstand2)
            }
            else -> sivilstand1 == sivilstand2
        }

        if (erLikSivilstand) {
            builder.append(builder.append("\n Person: sivilstand = V1: $sivilstand1, V2: $sivilstand2"))
        }


    }

}