package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.kontrakter.ef.søknad.Barn
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.søknad.domain.Barn as BarnDto

fun List<BarnDto>.tilSøknadsfelt(vedlegg: Map<String, DokumentasjonWrapper>): Søknadsfelt<List<Barn>> = BarnMapper.map(this, vedlegg)
