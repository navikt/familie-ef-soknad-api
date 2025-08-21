package no.nav.familie.ef.søknad.søknad

import no.nav.familie.ef.søknad.søknad.domain.Arbeidssøker
import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynGjenbrukDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadSkolepengerDto
import no.nav.familie.ef.søknad.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.søknad.mapper.SkjemaMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadBarnetilsynMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadOvergangsstønadMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadSkolepengerMapper
import no.nav.familie.kontrakter.ef.iverksett.SvarId
import no.nav.familie.kontrakter.felles.søknad.SistInnsendtSøknadDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SøknadService(
    private val mottakClient: MottakClient,
    private val overgangsstønadMapper: SøknadOvergangsstønadMapper,
    private val barnetilsynMapper: SøknadBarnetilsynMapper,
    private val skolepengerMapper: SøknadSkolepengerMapper,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun sendInnSøknadOvergangsstønad(
        søknad: SøknadOvergangsstønadDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = overgangsstønadMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = mottakClient.sendInnSøknadOvergangsstønad(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInnSøknadBarnetilsyn(
        søknad: SøknadBarnetilsynDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = barnetilsynMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = mottakClient.sendInnSøknadBarnetilsyn(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInnSøknadSkolepenger(
        søknad: SøknadSkolepengerDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = skolepengerMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = mottakClient.sendInnSøknadSkolepenger(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInnArbeidssøkerSkjema(
        arbeidssøker: Arbeidssøker,
        fnr: String,
        navn: String,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadDto = SkjemaMapper.mapTilKontrakt(arbeidssøker, fnr, navn, innsendingMottatt)
        val kvittering = mottakClient.sendInnArbeidssøkerSkjema(søknadDto)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun hentForrigeBarnetilsynSøknadKvittering(): SøknadBarnetilsynGjenbrukDto? = SøknadBarnetilsynMapper().mapTilDto(mottakClient.hentForrigeBarnetilsynSøknadKvittering())

    fun hentSistInnsendtSøknadPerStønad(): List<SistInnsendtSøknadDto> = mottakClient.hentSistInnsendtSøknadPerStønad()

    fun harSøknadGyldigeVerdier(søknadBarnetilsynGjenbrukDto: SøknadBarnetilsynGjenbrukDto?): Boolean? {
        return søknadBarnetilsynGjenbrukDto?.let { søknadBT ->
            søknadBT.person.barn
                .filter { it.forelder != null }
                .all { barn -> barn.forelder?.harDereSkriftligSamværsavtale?.harGyldigSvarId() ?: true && barn.forelder?.harAnnenForelderSamværMedBarn.harGyldigSvarId() && barn.forelder?.borAnnenForelderISammeHus.harGyldigSvarId() && barn.forelder?.hvorMyeSammen.harGyldigSvarId() }
        }
    }

    fun TekstFelt?.harGyldigSvarId(): Boolean = this?.svarid.kanMappesTilSvarIdEnum(this?.label ?: "ukjent TekstFelt")

    fun String?.kanMappesTilSvarIdEnum(feltNavn: String): Boolean {
        if (this == null) return true
        try {
            enumValueOf<SvarId>(this)
        } catch (e: IllegalArgumentException) {
            logger.error("Ugyldig svarId-verdi '$this' i felt '$feltNavn' \n ${e.message}")
            return false
        }

        return true
    }
}
