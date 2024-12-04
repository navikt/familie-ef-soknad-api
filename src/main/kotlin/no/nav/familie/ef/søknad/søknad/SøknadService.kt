package no.nav.familie.ef.søknad.søknad

import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynGjenbrukDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadSkolepengerDto
import no.nav.familie.ef.søknad.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadBarnetilsynMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadOvergangsstønadMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadSkolepengerMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SøknadService(
    private val mottakClient: MottakClient,
    private val overgangsstønadMapper: SøknadOvergangsstønadMapper,
    private val barnetilsynMapper: SøknadBarnetilsynMapper,
    private val skolepengerMapper: SøknadSkolepengerMapper,
) {
    fun sendInn(
        søknad: SøknadOvergangsstønadDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = overgangsstønadMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = mottakClient.sendInn(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInn(
        søknad: SøknadBarnetilsynDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = barnetilsynMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = mottakClient.sendInnBarnetilsynsøknad(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInn(
        søknad: SøknadSkolepengerDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = skolepengerMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = mottakClient.sendInnSkolepenger(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInnSøknadskvittering(
        søknad: SøknadOvergangsstønadDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = overgangsstønadMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = mottakClient.sendInnSøknadskvitteringOvergangsstønad(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInnSøknadskvitteringBarnetilsyn(
        søknad: SøknadBarnetilsynDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = barnetilsynMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = mottakClient.sendInnSøknadskvitteringBarnetilsyn(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInnSøknadskvitteringSkolepenger(
        søknad: SøknadSkolepengerDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = skolepengerMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = mottakClient.sendInnSøknadskvitteringSkolepenger(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun hentSøknadPdf(søknadId: String): ByteArray = mottakClient.hentSøknadKvittering(søknadId)

    fun hentForrigeBarnetilsynSøknad(): SøknadBarnetilsynGjenbrukDto? = SøknadBarnetilsynMapper().mapTilDto(mottakClient.hentForrigeBarnetilsynSøknad())
}
