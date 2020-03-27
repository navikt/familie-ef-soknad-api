package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Situasjon
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate

object SituasjonsMapper {




    private fun dokumentfelt(dokumentNavn: String, dokumenter: Map<String, Dokument>): Søknadsfelt<Dokument>? {
        val dokument = dokumenter[dokumentNavn]
        return dokument?.let {
            Søknadsfelt(dokument.tittel, dokument)
        }
    }

    fun mapSituasjon(frontendDto: SøknadDto, dokumenter: Map<String, Dokument>): Situasjon {
        return Situasjon(Søknadsfelt("Gjelder noe av dette deg?",
                                     listOf("Barnet mitt er sykt",
                                            "Jeg har søkt om barnepass, men ikke fått plass enda",
                                            "Jeg har barn som har behov for særlig tilsyn på grunn av fysiske, psykiske eller store sosiale problemer")),
                         dokumentfelt("Legeerklæring", dokumenter),
                         dokumentfelt("Legeattest for egen sykdom eller sykt barn", dokumenter),
                         dokumentfelt("Avslag på søknad om barnehageplass, skolefritidsordning e.l.", dokumenter),
                         dokumentfelt("Dokumentasjon av særlig tilsynsbehov", dokumenter),
                         dokumentfelt("Dokumentasjon av studieopptak", dokumenter),
                         Søknadsfelt("Når skal du starte i ny jobb?", LocalDate.of(2045, 12, 16)),
                         dokumentfelt("Dokumentasjon av jobbtilbud", dokumenter),
                         Søknadsfelt("Når skal du starte utdanningen?", LocalDate.of(2025, 7, 28)),
                         Søknadsfelt("Har du sagt opp jobben eller redusert arbeidstiden de siste 6 månedene?",
                                     "Ja, jeg har sagt opp jobben eller tatt frivillig permisjon (ikke foreldrepermisjon)"),
                         Søknadsfelt("Hvorfor sa du opp?", "Sjefen var dum"),
                         Søknadsfelt("Når sa du opp?", LocalDate.of(2014, 1, 12)),
                         dokumentfelt("Dokumentasjon av arbeidsforhold", dokumenter))
    }


}


