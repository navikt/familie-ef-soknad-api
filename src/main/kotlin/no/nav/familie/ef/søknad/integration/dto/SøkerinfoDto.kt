package no.nav.familie.ef.søknad.integration.dto

import no.nav.familie.ef.søknad.api.dto.Arbeidsforhold

class SøkerinfoDto(val person: PersonDto,
                   val arbeidsforhold: List<Arbeidsforhold>)
