package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.FamilieIntegrasjonerClient
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
internal class OppslagService(private val client: TpsInnsynServiceClient,
                              private val integrasjonerClient: FamilieIntegrasjonerClient,
                              private val regelverkConfig: RegelverkConfig) : Oppslag {

    override fun hentSøkerinfo(): Søkerinfo {
        val personinfoDto = client.hentPersoninfo()
        val barn = client.hentBarn()

        val aktuelleBarn = barn.filter { erIAktuellAlder(it.fødselsdato) && it.harSammeAdresse }

        return SøkerinfoMapper.mapTilSøkerinfo(personinfoDto, aktuelleBarn)
    }

    fun erIAktuellAlder(fødselsdato: LocalDate?): Boolean {
        if (fødselsdato == null) {
            return false
        }
        val alder = Period.between(fødselsdato, LocalDate.now())
        val alderIÅr = alder.years
        return alderIÅr <= regelverkConfig.alder.maks
    }

    override fun hentPoststedFor(postnummer: String): String? {
        return integrasjonerClient.hentPoststedFor(postnummer)
    }

}
