package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.kontrakter.ef.søknad.Bosituasjon
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate

object BosituasjonMapper {
    fun mapBosituasjon(frontendDto: SøknadDto, dokumenter: Map<String, Dokument>): Bosituasjon {
        return bosituasjon()
    }



    private fun bosituasjon(): Bosituasjon {
        return Bosituasjon(Søknadsfelt("Deler du bolig med andre voksne?",
                                       "Ja, jeg har samboer og lever i et ekteskapslignende forhold"),
                           Søknadsfelt("Om samboeren din",
                                       personMinimum()),
                           Søknadsfelt("Når flyttet dere sammen?", LocalDate.of(2018, 8, 12)))
    }

    private fun personMinimum(): PersonMinimum {
        return PersonMinimum(Søknadsfelt("Navn", "Bob Burger"),
                             null,
                             Søknadsfelt("Fødselsdato", LocalDate.of(1992, 2, 18)))
    }

}
