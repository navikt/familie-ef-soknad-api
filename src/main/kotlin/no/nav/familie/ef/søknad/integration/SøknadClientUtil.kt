package no.nav.familie.ef.søknad.integration

import no.nav.familie.kontrakter.ef.ettersending.SøknadMedDokumentasjonsbehovDto
import java.time.LocalDate

object SøknadClientUtil {

    fun filtrerVekkEldreDokumentasjonsbehov(søknader: List<SøknadMedDokumentasjonsbehovDto>): List<SøknadMedDokumentasjonsbehovDto> {
        val fireUker: Long = 4 * 7; /* dager */
        return søknader.filter { LocalDate.now().toEpochDay() - it.søknadDato.toEpochDay() < fireUker }
    }
}