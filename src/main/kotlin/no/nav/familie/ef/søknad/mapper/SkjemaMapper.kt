package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker
import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDateTime
import no.nav.familie.kontrakter.ef.søknad.Arbeidssøker as ArbeidssøkerKontrakt

object SkjemaMapper {

    fun mapTilKontrakt(arbeidssøker: Arbeidssøker,
                       fnr: String,
                       navn: String,
                       innsendingMottatt: LocalDateTime): SkjemaForArbeidssøker {
        val arbeidssøkerKontrakt = arbeidssøker.toArbeidssøkerKontrakt()
        return SkjemaForArbeidssøker(
                innsendingsdetaljer = Søknadsfelt("Innsendingsdetaljer",
                                                  Innsendingsdetaljer(Søknadsfelt("Dato mottatt", innsendingMottatt))),
                arbeidssøker = arbeidssøkerKontrakt,
                personaliaArbeidssøker = Søknadsfelt("NAV 15-08.01",
                                                     PersonaliaArbeidssøker(navn = Søknadsfelt("Navn", navn),
                                                                            fødselsnummer = Søknadsfelt("Fødselsnummer",
                                                                                                        Fødselsnummer(fnr)))
                )
        )
    }


}

private fun Arbeidssøker.toArbeidssøkerKontrakt(): Søknadsfelt<ArbeidssøkerKontrakt> {
    return Søknadsfelt("Enslig mor og far som er arbeidssøker",
                       ArbeidssøkerKontrakt(ønskerDuMinst50ProsentStilling = Søknadsfelt(ønskerSøker50ProsentStilling.label,
                                                                                         ønskerSøker50ProsentStilling.verdi),
                                            hvorØnskerDuArbeid = Søknadsfelt(hvorØnskerSøkerArbeid.label,
                                                                             hvorØnskerSøkerArbeid.verdi),
                                            kanDuSkaffeBarnepassInnenEnUke = kanSkaffeBarnepassInnenEnUke?.tilSøknadsfelt(),
                                            kanDuBegynneInnenEnUke = Søknadsfelt(kanBegynneInnenEnUke.label,
                                                                                 kanBegynneInnenEnUke.verdi),
                                            villigTilÅTaImotTilbudOmArbeid = Søknadsfelt(villigTilÅTaImotTilbudOmArbeid.label,
                                                                                         villigTilÅTaImotTilbudOmArbeid.verdi),
                                            registrertSomArbeidssøkerNav = Søknadsfelt(registrertSomArbeidssøkerNav.label,
                                                                                       registrertSomArbeidssøkerNav.verdi)))
}
