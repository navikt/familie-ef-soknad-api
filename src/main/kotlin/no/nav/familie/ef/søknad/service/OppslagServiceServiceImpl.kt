package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.ApiFeil
import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlAnnenForelder
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlSøker
import no.nav.familie.ef.søknad.integration.dto.pdl.visningsnavn
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import no.nav.familie.ef.søknad.mapper.harBeskyttetAdresse
import no.nav.familie.ef.søknad.mapper.harBeskyttetAdresseIkkeStreng
import no.nav.familie.ef.søknad.mapper.harStrengBeskyttetAdresse
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

        val andreForeldre = hentAndreForeldre(aktuelleBarn, søkersPersonIdent)

        validerAdressesperrePåSøkerMedRelasjoner(pdlSøker, aktuelleBarn, andreForeldre)

        return søkerinfoMapper.mapTilSøkerinfo(pdlSøker, aktuelleBarn, andreForeldre)

    }

    private fun validerAdressesperrePåSøkerMedRelasjoner(
            pdlSøker: PdlSøker,
            aktuelleBarn: Map<String, PdlBarn>,
            andreForeldre: Map<String, PdlAnnenForelder>
    ) {
        when {
            relasjonerHarAdressesperreSøkerHarIkkeAdressesperre(pdlSøker, aktuelleBarn, andreForeldre) -> throwException()
            relasjonerHarStrengereBeskyttelseEnnSøker(pdlSøker, aktuelleBarn, andreForeldre) -> throwException()
        }
    }

    private fun throwException() {
        throw ApiFeil("Personer med tilknytning til søker har adressesperre", HttpStatus.FORBIDDEN)
    }

    private fun relasjonerHarAdressesperreSøkerHarIkkeAdressesperre(
            pdlSøker: PdlSøker,
            aktuelleBarn: Map<String, PdlBarn>,
            andreForeldre: Map<String, PdlAnnenForelder>
    ): Boolean {

        return when {
            pdlSøker.harIkkeBeskyttetAdresse() && aktuelleBarn.harBeskyttetAdresse() -> true
            pdlSøker.harIkkeBeskyttetAdresse() && andreForeldre.harBeskyttetAdresse() -> true
            else -> false
        }

    }

    private fun relasjonerHarStrengereBeskyttelseEnnSøker(
            pdlSøker: PdlSøker,
            aktuelleBarn: Map<String, PdlBarn>,
            andreForeldre: Map<String, PdlAnnenForelder>
    ): Boolean {
        return when {
            pdlSøker.harBeskyttetAdresseIkkeStreng() && aktuelleBarn.harStrengBeskyttetAdresse() -> true
            pdlSøker.harBeskyttetAdresseIkkeStreng() && andreForeldre.harStrengBeskyttetAdresse() -> true
            else -> false
        }
    }

    override fun hentSøkerNavn(): String {
        val søkersPersonIdent = EksternBrukerUtils.hentFnrFraToken()
        val pdlSøker = pdlClient.hentSøker(søkersPersonIdent)
        return pdlSøker.navn.first().visningsnavn()
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

@JvmName("harStrengBeskyttetAdresseStringPdlAnnenForelder")
private fun Map<String, PdlAnnenForelder>.harStrengBeskyttetAdresse(): Boolean =
        filter { it.value.adressebeskyttelse.harStrengBeskyttetAdresse() }.any()


private fun Map<String, PdlBarn>.harStrengBeskyttetAdresse(): Boolean =
        filter { it.value.adressebeskyttelse.harStrengBeskyttetAdresse() }.any()


@JvmName("harBeskyttetAdresseStringPdlAnnenForelder")
private fun Map<String, PdlAnnenForelder>.harBeskyttetAdresse(): Boolean =
        filter { it.value.adressebeskyttelse.harBeskyttetAdresse() }.any()

private fun Map<String, PdlBarn>.harBeskyttetAdresse(): Boolean =
        filter { it.value.adressebeskyttelse.harBeskyttetAdresse() }.any()

private fun PdlSøker.harIkkeBeskyttetAdresse(): Boolean = !this.adressebeskyttelse.harBeskyttetAdresse()
private fun PdlSøker.harBeskyttetAdresseIkkeStreng(): Boolean = this.adressebeskyttelse.harBeskyttetAdresseIkkeStreng()




