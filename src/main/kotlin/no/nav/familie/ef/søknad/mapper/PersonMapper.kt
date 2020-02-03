package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.tps.Adresse
import no.nav.familie.ef.søknad.api.dto.tps.Barn
import no.nav.familie.ef.søknad.api.dto.tps.Person
import no.nav.familie.ef.søknad.integration.dto.AdresseinfoDto
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto

object PersonMapper {

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
                      personinfoDto.statsborgerskap?.kode?.verdi ?: "")
    }

    private fun mapTilAdresse(adresseinfoDto: AdresseinfoDto?): Adresse {
        return Adresse(adresseinfoDto?.bostedsadresse?.adresse
                       ?: "",
                       adresseinfoDto?.bostedsadresse?.postnummer ?: "")
    }
}