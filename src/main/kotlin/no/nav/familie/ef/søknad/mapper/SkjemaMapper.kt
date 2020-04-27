package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.SkjemaForArbeidssøker
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.springframework.stereotype.Component
import no.nav.familie.kontrakter.ef.søknad.Arbeidssøker as ArbeidssøkerKontrakt

@Component
object SkjemaMapper {

    fun mapTilKontrakt(arbeidssøker: Arbeidssøker, fnr: String): SkjemaForArbeidssøker {
        val arbeidssøkerKontrakt = arbeidssøker.toArbeidssøkerKontrakt()
        return SkjemaForArbeidssøker(fødselsnummer = Søknadsfelt("Fødselsnummer",
                                                                 Fødselsnummer(fnr)),
                                     arbeidssøker = arbeidssøkerKontrakt)
    }
}

private fun Arbeidssøker.toArbeidssøkerKontrakt(): Søknadsfelt<ArbeidssøkerKontrakt> {
    return Søknadsfelt("Arbeidssøker",
                       ArbeidssøkerKontrakt(ønskerDuMinst50ProsentStilling = Søknadsfelt(ønskerSøker50ProsentStilling.label,
                                                                                         ønskerSøker50ProsentStilling.verdi),
                                    hvorØnskerDuArbeid = Søknadsfelt(hvorØnskerSøkerArbeid.label, hvorØnskerSøkerArbeid.verdi),
                                    kanDuSkaffeBarnepassInnenEnUke = Søknadsfelt(kanSkaffeBarnepassInnenEnUke.label,
                                                                                 kanSkaffeBarnepassInnenEnUke.verdi),
                                    kanDuBegynneInnenEnUke = Søknadsfelt(kanBegynneInnenEnUke.label,
                                                                         kanBegynneInnenEnUke.verdi),
                                    villigTilÅTaImotTilbudOmArbeid = Søknadsfelt(villigTilÅTaImotTilbudOmArbeid.label,
                                                                                 villigTilÅTaImotTilbudOmArbeid.verdi),
                                    registrertSomArbeidssøkerNav = Søknadsfelt(registrertSomArbeidssøkerNav.label,
                                                                               registrertSomArbeidssøkerNav.verdi)))
}
