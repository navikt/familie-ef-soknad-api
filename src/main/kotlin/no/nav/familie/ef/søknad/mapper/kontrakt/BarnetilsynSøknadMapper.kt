package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BarnetilsynDto
import no.nav.familie.ef.søknad.integration.BarnetilsynRequestData
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.lagDokumentasjonWrapper
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.*
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BarnetilsynSøknadMapper(private val dokumentServiceService: DokumentService) {

    fun mapTilIntern(barnetilsynDto: BarnetilsynDto,
                     innsendingMottatt: LocalDateTime): BarnetilsynRequestData {
        val vedleggData: Map<String, ByteArray> = dokumentServiceService.hentDokumenter(barnetilsynDto.dokumentasjonsbehov)
        val vedlegg: Map<String, DokumentasjonWrapper> = lagDokumentasjonWrapper(barnetilsynDto.dokumentasjonsbehov)

        val barnetilsynSøknad = BarnetilsynSøknadKontrakt(innsendingsDetaljer(innsendingMottatt),
                                                          barnetilsynDto.person.søker.tilSøknadsFelt(),
                                                          barnetilsynDto.person.barn.tilSøknadsfelt(vedlegg))

        return BarnetilsynRequestData(SøknadBarnetilsyn(barnetilsynSøknad, vedlegg.values.map { it.vedlegg }.flatten()),
                                      vedleggData)
    }

    private fun innsendingsDetaljer(innsendingMottatt: LocalDateTime): Søknadsfelt<Innsendingsdetaljer> {
        return Søknadsfelt("Innsendingsdetaljer",
                           Innsendingsdetaljer(Søknadsfelt("Dato mottatt",
                                                           innsendingMottatt)))
    }

}


// TODO flyttes til kontrakter
data class SøknadBarnetilsyn(val søknad: BarnetilsynSøknadKontrakt, val vedlegg: List<Vedlegg>)
data class BarnetilsynSøknadKontrakt(
        val innsendingsdetaljer: Søknadsfelt<Innsendingsdetaljer>,
        val personalia: Søknadsfelt<Personalia>,
        val barn: Søknadsfelt<List<Barn>>)


