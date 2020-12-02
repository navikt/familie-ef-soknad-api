package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.tps.Adresse
import no.nav.familie.ef.søknad.api.dto.tps.Barn
import no.nav.familie.ef.søknad.api.dto.tps.Person
import org.slf4j.LoggerFactory
import kotlin.reflect.full.memberProperties

@Deprecated("Denne skal fjernes så snart vi er helt over på PDL")
object OppslagServiceLoggHjelper {


    private val secureLogger = LoggerFactory.getLogger("secureLogger")
    private val logger = LoggerFactory.getLogger(this::class.java)


    fun logDiff(søkerinfoTps: Søkerinfo, søkerinfoPdl: Søkerinfo): String {
        val builder = StringBuilder()
        leggTilSøkerDiff(søkerinfoTps, søkerinfoPdl, builder)
        leggTilBarneDiff(søkerinfoTps, søkerinfoPdl, builder)
        if (builder.isNotBlank()) {
            secureLogger.warn(builder.toString())
            logger.warn("Diff funnet mellom tps/pdl data. Se securelogs for detaljer")
        }
        return builder.toString()
    }

    private fun leggTilBarneDiff(søkerinfoTps: Søkerinfo,
                                 søkerinfoPdl: Søkerinfo,
                                 builder: StringBuilder) {
        if (søkerinfoTps.barn.size != søkerinfoPdl.barn.size) {
            builder.append("Ikke samme antall barn fra tps og pdl: \n Tps: ${søkerinfoTps.barn}, \n V2: ${søkerinfoPdl.barn}")
        } else {
            val barn1Liste = søkerinfoTps.barn.sortedBy { it.fnr }
            val barn2Liste = søkerinfoPdl.barn.sortedBy { it.fnr }
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
                        builder.append("\n Barn: ${prop.name} = Tps: ${prop.get(it.first)}, V2: ${prop.get(it.second)}")
                    }
                }
            }
        }
    }

    private fun listOfSeparatedNames(name: String) =
            name.split(" ").sorted()

    private fun leggTilSøkerDiff(søkerinfoTps: Søkerinfo,
                                 søkerinfoPdl: Søkerinfo,
                                 builder: StringBuilder) {
        for (prop in Person::class.memberProperties) {
            val property1 = prop.get(søkerinfoTps.søker)
            val property2 = prop.get(søkerinfoPdl.søker)
            when (prop.name) {
                "sivilstand" -> {
                    if (erUlikSivilstand(søkerinfoTps.søker.sivilstand, søkerinfoPdl.søker.sivilstand)) {
                        builder.append(builder.append("\n Person: sivilstand = Tps: ${søkerinfoTps.søker.sivilstand}, Pdl: ${søkerinfoPdl.søker.sivilstand}"))
                    }
                }
                "adresse" -> {
                    if (erUlikAdresse(søkerinfoTps.søker.adresse, søkerinfoPdl.søker.adresse)) {
                        builder.append("\n Person: ${prop.name} = Tps: $property1, Pdl: $property2")
                    }
                }
                else -> {
                    if (property1 != property2) {
                        builder.append("\n Person: ${prop.name} = Tps: $property1, Pdl: $property2")
                    }
                }
            }
        }
    }

    private fun erUlikAdresse(adresseTps: Adresse,
                              adressePdl: Adresse): Boolean {
        val postnummerDiff = adresseTps.postnummer != adressePdl.postnummer
        val postStedDiff = adresseTps.poststed != adressePdl.poststed
        val adresseDiff = !adressePdl.adresse.startsWith(adresseTps.adresse, ignoreCase = true)
        return postStedDiff or postnummerDiff or adresseDiff
    }


    private fun erUlikSivilstand(tpsSivilstand: String, pdlSivilstand: String): Boolean {
        val tpsTilPdlSivilstand = when (tpsSivilstand) {
            "REPA" -> "PARTNER"
            "SEPA" -> "SEPARERT_PARTNER"
            "SEPR" -> "SEPARERT"
            "GJPA" -> "GJENLEVENDE_PARTNER"
            "UGIF" -> "UGIFT"
            "SAMB" -> "UGIFT"
            "SKIL" -> "SKILT"
            "ENKE" -> "ENKE_ELLER_ENKEMANN"
            else -> tpsSivilstand
        }
        return tpsTilPdlSivilstand != pdlSivilstand
    }

}