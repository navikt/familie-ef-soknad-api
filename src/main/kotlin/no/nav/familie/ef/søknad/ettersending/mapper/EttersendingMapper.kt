package no.nav.familie.ef.søknad.ettersending.mapper

import no.nav.familie.kontrakter.ef.ettersending.EttersendelseDto
import no.nav.familie.kontrakter.felles.ef.StønadType
import java.time.LocalDateTime

object EttersendingMapper {

    fun groupByStønad(
        dto: EttersendelseDto,
        innsendingMottatt: LocalDateTime,
    ): Map<StønadType, EttersendelseDto> {
        return dto.dokumentasjonsbehov
            .map { it.copy(innsendingstidspunkt = innsendingMottatt) }
            .groupBy { it.stønadType }
            .mapValues { entry -> EttersendelseDto(personIdent = dto.personIdent, dokumentasjonsbehov = entry.value) }
    }
}
