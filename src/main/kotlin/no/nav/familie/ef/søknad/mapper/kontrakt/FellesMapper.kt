package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.mapper.Språktekster.DatoMottatt
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.kontrakter.ef.søknad.Innsendingsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate
import java.time.LocalDateTime
import no.nav.familie.ef.søknad.mapper.Språktekster.Innsendingsdetaljer as InnsendingsdetaljerTekst

object FellesMapper {

    fun mapInnsendingsdetaljer(innsendingMottatt: LocalDateTime, datoPåbegyntSøknad: LocalDate? = null): Søknadsfelt<Innsendingsdetaljer> =
        Søknadsfelt(
            InnsendingsdetaljerTekst.hentTekst(),
            Innsendingsdetaljer(Søknadsfelt(DatoMottatt.hentTekst(), innsendingMottatt), datoPåbegyntSøknad),
        )
}
