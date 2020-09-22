package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.kontrakter.ef.søknad.Barn
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barn as BarnDto

fun List<BarnDto>.tilSøknadsfelt(vedlegg: Map<String, DokumentasjonWrapper>): Søknadsfelt<List<Barn>> {
    return BarnMapper.map(this, vedlegg)
}
