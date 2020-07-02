package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.tps.Adresse
import no.nav.familie.ef.søknad.api.dto.tps.Barn
import no.nav.familie.ef.søknad.api.dto.tps.Person
import no.nav.familie.ef.søknad.integration.dto.AdresseinfoDto
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.service.KodeverkService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
internal class SøkerinfoMapper(private val kodeverkService: KodeverkService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun mapTilSøkerinfo(personinfoDto: PersoninfoDto, aktuelleBarn: List<RelasjonDto>): Søkerinfo {
        return Søkerinfo(mapTilPerson(personinfoDto),
                         aktuelleBarn.map(this::mapTilBarn))
    }

    fun mapTilBarn(relasjonDto: RelasjonDto): Barn {
        return Barn(relasjonDto.ident,
                    relasjonDto.forkortetNavn,
                    relasjonDto.alder,
                    relasjonDto.fødselsdato,
                    relasjonDto.harSammeAdresse)
    }


    fun mapTilPerson(personinfoDto: PersoninfoDto): Person {
        return Person(personinfoDto.ident,
                      personinfoDto.navn.forkortetNavn,
                      mapTilAdresse(personinfoDto.adresseinfo),
                      personinfoDto.egenansatt?.isErEgenansatt ?: false,
                      personinfoDto.sivilstand?.kode?.verdi ?: "",
                      hentLand(personinfoDto.statsborgerskap?.kode?.verdi))
    }

    private fun mapTilAdresse(adresseinfoDto: AdresseinfoDto?): Adresse {
        val postnummer: String? = adresseinfoDto?.bostedsadresse?.postnummer
        return Adresse(adresse = adresseinfoDto?.bostedsadresse?.adresse
                                 ?: "",
                       postnummer = postnummer ?: "",
                       poststed = hentPoststed(postnummer))
    }

    private fun hentPoststed(postnummer: String?): String {
        return hentKodeverdi("poststed", postnummer, kodeverkService::hentPoststed)
    }

    private fun hentLand(landkode: String?): String {
        return hentKodeverdi("land", landkode, kodeverkService::hentLand)
    }

    private fun hentKodeverdi(type: String, kode: String?, hentKodeverdiFunction: Function1<String, String?>): String {
        return try {
            kode?.let(hentKodeverdiFunction) ?: ""
        } catch (e: Exception) {
            //Ikke la feil fra integrasjon stoppe henting av data
            logger.error("Feilet henting av $type til $kode message=${e.message} cause=${e.cause?.message}")
            ""
        }
    }
}
