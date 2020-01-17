package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.SøknadInput
import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDate
import java.time.Month

object SøknadMapper {

    // TODO Få inn data og map det korrekt.

    fun mapTilIntern(søknad: SøknadInput): Søknad {
        return søknad()
    }


    private fun søknad() =
            Søknad(Personalia(Fødselsnummer("24028813909"), "", "", Adresse("", 0, "", "", "", "", ""), "", ""),
                   Sivilstandsdetaljer(spørsmålBoolean,
                                       dokument("a"),
                                       spørsmålBoolean,
                                       dokument("b"),
                                       spørsmålBoolean,
                                       spørsmålDate,
                                       dokument("c"),
                                       spørsmålString,
                                       dokument("d"),
                                       date,
                                       date,
                                       ""),
                   Medlemskapsdetaljer(spørsmålBoolean, spørsmålBoolean, null, spørsmålBoolean, dokument("e")),
                   Bosituasjon(spørsmålString, null, spørsmålDate),
                   Sivilstandsplaner(spørsmålBoolean, spørsmålDate, null),
                   null,
                   listOf(KommendeBarn("",
                                       "",
                                       null,
                                       Samvær(spørsmålBoolean,
                                              dokument("f"),
                                              spørsmålString,
                                              spørsmålString,
                                              dokument("g"),
                                              spørsmålString,
                                              spørsmålBoolean,
                                              spørsmålBoolean,
                                              spørsmålDate,
                                              dokument("h"),
                                              spørsmålString,
                                              spørsmålString),
                                       spørsmålBoolean,
                                       date,
                                       spørsmålBoolean,
                                       dokument("j"))),
                   Aktivitet(spørsmålList, null, null, null, null, null),
                   Situasjon(spørsmålList,
                             dokument("i"),
                             dokument("j"),
                             dokument("k"),
                             dokument("l"),
                             dokument("m"),
                             spørsmålDate,
                             dokument("n"),
                             spørsmålDate,
                             spørsmålString,
                             spørsmålString,
                             spørsmålDate,
                             dokument("o")),
                   Stønadsstart(Month.AUGUST, 2014))

    private fun dokument(tittel: String) = Dokument(Fil(byteArrayOf(12)), tittel)

    private val date = LocalDate.now()

    private val spørsmålList = Spørsmål("", listOf(""))

    private val spørsmålBoolean = Spørsmål("", true)

    private val spørsmålDate = Spørsmål("", LocalDate.now())

    private val spørsmålString = Spørsmål("", "")


}