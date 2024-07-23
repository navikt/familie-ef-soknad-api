package no.nav.familie.ef.søknad.søknad

import no.nav.familie.kontrakter.ef.ettersending.SøknadMedDokumentasjonsbehovDto
import java.time.LocalDate

object SøknadClientUtil {
    fun filtrerVekkEldreDokumentasjonsbehov(søknader: List<SøknadMedDokumentasjonsbehovDto>): List<SøknadMedDokumentasjonsbehovDto> = søknader.filter { it.søknadDato.plusWeeks(4).isAfter(LocalDate.now()) }
}
