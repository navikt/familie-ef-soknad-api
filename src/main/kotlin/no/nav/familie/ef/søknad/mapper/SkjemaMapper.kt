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
        kontekst.set(Språk.fromString(arbeidssøker.locale))
        val arbeidssøkerKontrakt = arbeidssøker.toArbeidssøkerKontrakt()
        return SkjemaForArbeidssøker(
                innsendingsdetaljer = Søknadsfelt(Språktekster.Innsendingsdetaljer.hentTekst(),
                                                  Innsendingsdetaljer(Søknadsfelt(Språktekster.DatoMottatt.hentTekst(),
                                                                                  innsendingMottatt))),
                arbeidssøker = arbeidssøkerKontrakt,
                personaliaArbeidssøker = Søknadsfelt("NAV 15-08.01",
                                                     PersonaliaArbeidssøker(navn = Søknadsfelt(Språktekster.Navn.hentTekst(),
                                                                                               navn),
                                                                            fødselsnummer = Søknadsfelt(Språktekster.Fødselsnummer.hentTekst(),
                                                                                                        Fødselsnummer(fnr)))
                )
        )
    }


}

private fun Arbeidssøker.toArbeidssøkerKontrakt(): Søknadsfelt<ArbeidssøkerKontrakt> {
    return Søknadsfelt(Språktekster.EnsligMorEllerFarSomErArbeidssøker.hentTekst(),
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
