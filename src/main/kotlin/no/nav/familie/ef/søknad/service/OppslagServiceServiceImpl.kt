package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.ef.søknad.integration.dto.pdl.Adressebeskyttelse
import no.nav.familie.ef.søknad.integration.dto.pdl.AdressebeskyttelseGradering
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlAnnenForelder
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
internal class OppslagServiceServiceImpl(
        private val pdlClient: PdlClient,
        private val pdlStsClient: PdlStsClient,
        private val regelverkConfig: RegelverkConfig,
        private val søkerinfoMapper: SøkerinfoMapper,
) : OppslagService {


    private val kreverAdressebeskyttelse = listOf(AdressebeskyttelseGradering.FORTROLIG,
                                                  AdressebeskyttelseGradering.STRENGT_FORTROLIG,
                                                  AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND)

    private val logger = LoggerFactory.getLogger(this::class.java)


    override fun hentSøkerinfo(): Søkerinfo {

        val søkersPersonIdent = EksternBrukerUtils.hentFnrFraToken()
        val pdlSøker = pdlClient.hentSøker(søkersPersonIdent)
        val barnIdentifikatorer = pdlSøker.familierelasjoner
                .filter { it.relatertPersonsRolle == Familierelasjonsrolle.BARN }
                .map { it.relatertPersonsIdent }
        val pdlBarn = pdlStsClient.hentBarn(barnIdentifikatorer)
        val aktuelleBarn = pdlBarn
                .filter { erIAktuellAlder(it.value.fødsel.first().fødselsdato) }
                .filter { erILive(it.value) }
                .filter { harIkkeBeskyttetAdresse(it.value.adressebeskyttelse) }

        val andreForeldre = hentAndreForeldre(aktuelleBarn, søkersPersonIdent)
        return søkerinfoMapper.mapTilSøkerinfo(pdlSøker, aktuelleBarn, andreForeldre)

    }

    private fun hentAndreForeldre(aktuelleBarn: Map<String, PdlBarn>,
                                  søkersPersonIdent: String): Map<String, PdlAnnenForelder> {
        return aktuelleBarn.map { it.value.familierelasjoner }
                .flatten()
                .filter { it.relatertPersonsIdent != søkersPersonIdent && it.relatertPersonsRolle != Familierelasjonsrolle.BARN }
                .map { it.relatertPersonsIdent }
                .distinct()
                .let { pdlStsClient.hentAndreForeldre(it) }
    }


    private fun harIkkeBeskyttetAdresse(adressebeskyttelse: List<Adressebeskyttelse>): Boolean {
        return !kreverAdressebeskyttelse.contains(adressebeskyttelse.firstOrNull()?.gradering
                                                  ?: AdressebeskyttelseGradering.UGRADERT)
    }

    fun erILive(pdlBarn: PdlBarn) =
            pdlBarn.dødsfall.firstOrNull()?.dødsdato == null


    fun erIAktuellAlder(fødselsdato: LocalDate?): Boolean {
        if (fødselsdato == null) {
            return false
        }
        val alder = Period.between(fødselsdato, LocalDate.now())
        val alderIÅr = alder.years
        return alderIÅr <= regelverkConfig.alder.maks
    }

}



