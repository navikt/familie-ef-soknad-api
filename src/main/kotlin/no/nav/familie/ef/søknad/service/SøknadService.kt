package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadSkolepengerDto
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadBarnetilsynMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadOvergangsstønadMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadSkolepengerMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SøknadService(private val søknadClient: SøknadClient,
                    private val dokumentServiceService: DokumentService,
                    private val overgangsstønadMapper: SøknadOvergangsstønadMapper,
                    private val barnetilsynMapper: SøknadBarnetilsynMapper,
                    private val skolepengerMapper: SøknadSkolepengerMapper,
                    private val featureToggleService: FeatureToggleService) {

    fun sendInn(søknad: SøknadOvergangsstønadDto,
                innsendingMottatt: LocalDateTime): Kvittering {
        val søknadRequestData = overgangsstønadMapper.mapTilIntern(søknad, innsendingMottatt, skalHenteVedlegg())
        val kvittering = sendInn(søknadRequestData, søknadClient::sendInnOld, søknadClient::sendInn)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInn(søknad: SøknadBarnetilsynDto, innsendingMottatt: LocalDateTime): Kvittering {
        val søknadRequestData = barnetilsynMapper.mapTilIntern(søknad, innsendingMottatt, skalHenteVedlegg())
        val kvittering =
                sendInn(søknadRequestData, søknadClient::sendInnBarnetilsynsøknadOld, søknadClient::sendInnBarnetilsynsøknad)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInn(søknad: SøknadSkolepengerDto, innsendingMottatt: LocalDateTime): Kvittering {
        val søknadRequestData = skolepengerMapper.mapTilIntern(søknad, innsendingMottatt, skalHenteVedlegg())
        val kvittering = sendInn(søknadRequestData, søknadClient::sendInnSkolepengerOld, søknadClient::sendInnSkolepenger)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    private fun <T, R> sendInn(request: T, v1: (T) -> R, v2: (T) -> R): R {
        return if (skalHenteVedlegg()) {
            v1.invoke(request)
        } else {
            v2.invoke(request)
        }
    }

    private fun skalHenteVedlegg() =
            !featureToggleService.isEnabled("familie.ef.soknad.api.dokumentFraMottak")

}
