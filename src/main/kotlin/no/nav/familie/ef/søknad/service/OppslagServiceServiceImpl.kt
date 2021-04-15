package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.ApiFeil
import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlAnnenForelder
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.visningsnavn
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.springframework.http.HttpStatus
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
        //  .filter { harIkkeBeskyttetAdresse(it.value.adressebeskyttelse) }

        val andreForeldre = hentAndreForeldre(aktuelleBarn, søkersPersonIdent)
        val søkerinfo = søkerinfoMapper.mapTilSøkerinfo(pdlSøker, aktuelleBarn, andreForeldre)

        validerAdressesperreOk(søkerinfo)

        return søkerinfo

    }

    override fun hentSøkerNavn(): String {
        val søkersPersonIdent = EksternBrukerUtils.hentFnrFraToken()
        val pdlSøker = pdlClient.hentSøker(søkersPersonIdent)
        return pdlSøker.navn.first().visningsnavn()
    }

    private fun validerAdressesperreOk(søkerinfo: Søkerinfo) {
        val adressesperreFunnet = if (!søkerinfo.søker.harAdressesperre) {
            søkerinfo.barn.any() {
                it.harAdressesperre || it.medForelder?.harAdressesperre ?: false
            }
        } else false

        if (adressesperreFunnet) {
            throw ApiFeil("Personer med tilknytning til søker har adressesperre", HttpStatus.FORBIDDEN)
        }
    }

    private fun hentAndreForeldre(
        aktuelleBarn: Map<String, PdlBarn>,
        søkersPersonIdent: String
    ): Map<String, PdlAnnenForelder> {
        return aktuelleBarn.map { it.value.familierelasjoner }
            .flatten()
            .filter { it.relatertPersonsIdent != søkersPersonIdent && it.relatertPersonsRolle != Familierelasjonsrolle.BARN }
            .map { it.relatertPersonsIdent }
            .distinct()
            .let { pdlStsClient.hentAndreForeldre(it) }
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



