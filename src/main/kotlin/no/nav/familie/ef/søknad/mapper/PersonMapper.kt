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
                personinfoDto.innvandringUtvandring?.innvandretDato,
                personinfoDto.innvandringUtvandring?.utvandretDato,
                personinfoDto.oppholdstillatelse?.kode?.verdi ?: "",
                personinfoDto.sivilstand?.kode?.verdi ?: "",
                personinfoDto.språk?.kode?.verdi ?: "bm",
                personinfoDto.statsborgerskap?.kode?.verdi ?: "",
                personinfoDto.telefon?.privat ?: "",
                personinfoDto.telefon?.mobil ?: "",
                personinfoDto.telefon?.jobb ?: "",
                personinfoDto.kontonummer?.nummer ?: "")
    }

    private fun mapTilAdresse(adresseinfoDto: AdresseinfoDto?): Adresse {
        return Adresse(adresseinfoDto?.bostedsadresse?.adresse
                ?: "",
                adresseinfoDto?.bostedsadresse?.adressetillegg ?: "",
                adresseinfoDto?.bostedsadresse?.kommune ?: "",
                adresseinfoDto?.bostedsadresse?.postnummer ?: "")
    }
}