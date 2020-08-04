package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Søker
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.kontrakter.ef.søknad.Barn
import no.nav.familie.kontrakter.ef.søknad.Personalia
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn as BarnDto

fun Søker.tilSøknadsFelt(): Søknadsfelt<Personalia> {
    return Søknadsfelt("Søker", PersonaliaMapper.mapPersonalia(this))
}

fun List<BarnDto>.tilSøknadsfelt(vedlegg: Map<String, DokumentasjonWrapper>): Søknadsfelt<List<Barn>> {
    return Søknadsfelt("Barna dine",
                       BarnMapper.mapBarn(this, vedlegg))
}