package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.utils.Språktekster.DatoMottatt
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.kontrakter.ef.søknad.Innsendingsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate
import java.time.LocalDateTime
import no.nav.familie.ef.søknad.utils.Språktekster.Innsendingsdetaljer as InnsendingsdetaljerTekst

object FellesMapper {

    fun mapInnsendingsdetaljer(innsendingMottatt: LocalDateTime, datoPåbegyntSøknad: LocalDate? = null): Søknadsfelt<Innsendingsdetaljer> =
        Søknadsfelt(
            InnsendingsdetaljerTekst.hentTekst(),
            Innsendingsdetaljer(Søknadsfelt(DatoMottatt.hentTekst(), innsendingMottatt), datoPåbegyntSøknad),
        )
}
