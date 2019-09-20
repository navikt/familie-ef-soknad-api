package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.AktørId
import no.nav.familie.ef.søknad.api.dto.Person
import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.integration.OppslagClient
import no.nav.familie.ef.søknad.mapper.PersonMapper
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = ["stub.oppslag"], havingValue = "false", matchIfMissing = true)
class OppslagService (private val client: OppslagClient) : Oppslag {

    override fun hentPerson(): Person {
        return PersonMapper.mapTilEkstern(client.hentPerson())
    }

    override fun hentSøkerinfo(): Søkerinfo {
        return SøkerinfoMapper.mapTilEkstern(client.hentSøkerInfo())
    }

    @Cacheable(cacheNames = ["aktor"])
    override fun hentAktørId(fnr: String): AktørId {
        return client.hentAktørId(fnr)
    }

    override fun toString(): String {
        return "OppslagTjeneste(connection=$client)"
    }


}
