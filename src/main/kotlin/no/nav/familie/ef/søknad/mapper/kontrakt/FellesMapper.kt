package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.kontrakter.ef.søknad.Innsendingsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDateTime

object FellesMapper {

    fun mapInnsendingsdetaljer(innsendingMottatt: LocalDateTime): Søknadsfelt<Innsendingsdetaljer> =
            Søknadsfelt("Innsendingsdetaljer", Innsendingsdetaljer(Søknadsfelt("Dato mottatt", innsendingMottatt)))
}
