package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.Arbeidssøker
import no.nav.familie.ef.søknad.utils.Språk
import no.nav.familie.ef.søknad.utils.Språktekster
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.kontekst
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Innsendingsdetaljer
import no.nav.familie.kontrakter.ef.søknad.PersonaliaArbeidssøker
import no.nav.familie.kontrakter.ef.søknad.SkjemaForArbeidssøker
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.felles.Fødselsnummer
import java.time.LocalDateTime
import no.nav.familie.kontrakter.ef.søknad.Arbeidssøker as ArbeidssøkerKontrakt

object SkjemaMapper {
    fun mapTilKontrakt(
        arbeidssøker: Arbeidssøker,
        fnr: String,
        navn: String,
        innsendingMottatt: LocalDateTime,
    ): SkjemaForArbeidssøker {
        kontekst.set(Språk.fromString(arbeidssøker.locale))
        val arbeidssøkerKontrakt = arbeidssøker.toArbeidssøkerKontrakt()
        return SkjemaForArbeidssøker(
            innsendingsdetaljer =
                Søknadsfelt(
                    Språktekster.Innsendingsdetaljer.hentTekst(),
                    Innsendingsdetaljer(
                        Søknadsfelt(
                            Språktekster.DatoMottatt.hentTekst(),
                            innsendingMottatt,
                        ),
                    ),
                ),
            arbeidssøker = arbeidssøkerKontrakt,
            personaliaArbeidssøker =
                Søknadsfelt(
                    Språktekster.Søker.hentTekst(),
                    PersonaliaArbeidssøker(
                        navn =
                            Søknadsfelt(
                                Språktekster.Navn.hentTekst(),
                                navn,
                            ),
                        fødselsnummer =
                            Søknadsfelt(
                                Språktekster.Fødselsnummer.hentTekst(),
                                Fødselsnummer(fnr),
                            ),
                    ),
                ),
        )
    }
}

fun Arbeidssøker.toArbeidssøkerKontrakt(): Søknadsfelt<ArbeidssøkerKontrakt> =
    Søknadsfelt(
        Språktekster.EnsligMorEllerFarSomErArbeidssøker.hentTekst(),
        ArbeidssøkerKontrakt(
            ønskerDuMinst50ProsentStilling =
                Søknadsfelt(
                    ønskerSøker50ProsentStilling.label,
                    ønskerSøker50ProsentStilling.verdi,
                ),
            hvorØnskerDuArbeid =
                Søknadsfelt(
                    hvorØnskerSøkerArbeid.label,
                    hvorØnskerSøkerArbeid.verdi,
                ),
            kanDuSkaffeBarnepassInnenEnUke = kanSkaffeBarnepassInnenEnUke?.tilSøknadsfelt(),
            kanDuBegynneInnenEnUke =
                Søknadsfelt(
                    kanBegynneInnenEnUke.label,
                    kanBegynneInnenEnUke.verdi,
                ),
            villigTilÅTaImotTilbudOmArbeid =
                Søknadsfelt(
                    villigTilÅTaImotTilbudOmArbeid.label,
                    villigTilÅTaImotTilbudOmArbeid.verdi,
                ),
            registrertSomArbeidssøkerNav =
                Søknadsfelt(
                    registrertSomArbeidssøkerNav.label,
                    registrertSomArbeidssøkerNav.verdi,
                ),
        ),
    )
