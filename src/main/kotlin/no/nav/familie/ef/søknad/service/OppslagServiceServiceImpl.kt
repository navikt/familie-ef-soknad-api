package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.ApiFeil
import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.UnsecurePdlClient
import no.nav.familie.ef.søknad.integration.dto.pdl.AdressebeskyttelseGradering
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlAnnenForelder
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlSøker
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
        private val unsecurePdlStsClient: UnsecurePdlClient,
        private val regelverkConfig: RegelverkConfig,
        private val søkerinfoMapper: SøkerinfoMapper,
) : OppslagService {


    override fun hentSøkerinfo(): Søkerinfo {

        val søkersPersonIdent = EksternBrukerUtils.hentFnrFraToken()
        val pdlSøker = pdlClient.hentSøker(søkersPersonIdent)
        val barnIdentifikatorer = pdlSøker.forelderBarnRelasjon
            .filter { it.relatertPersonsRolle == Familierelasjonsrolle.BARN }
            .map { it.relatertPersonsIdent }
        val pdlBarn = unsecurePdlStsClient.hentBarn(barnIdentifikatorer)
        val aktuelleBarn = pdlBarn
            .filter { erIAktuellAlder(it.value.fødsel.first().fødselsdato) }
                .filter { erILive(it.value) }

        val andreForeldre = hentAndreForeldre(aktuelleBarn, søkersPersonIdent)
        validerAdressesperrePåSøkerMedRelasjoner(pdlSøker, aktuelleBarn, andreForeldre)
        return søkerinfoMapper.mapTilSøkerinfo(pdlSøker, aktuelleBarn, andreForeldre)

    }

    private fun throwException() {
        throw ApiFeil("adressesperre", HttpStatus.FORBIDDEN)
    }

    private fun validerAdressesperrePåSøkerMedRelasjoner(
            pdlSøker: PdlSøker,
            aktuelleBarn: Map<String, PdlBarn>,
            andreForeldre: Map<String, PdlAnnenForelder>
    ) {
        val søkernivå = adresseNivå(pdlSøker.adressebeskyttelse.firstOrNull()?.gradering)
        val barnNivå = aktuelleBarn.values.maxOfOrNull { adresseNivå(it.adressebeskyttelse.firstOrNull()?.gradering) } ?: 0
        val andreForeldreNivå =
                andreForeldre.values.maxOfOrNull { adresseNivå(it.adressebeskyttelse.firstOrNull()?.gradering) } ?: 0
        if (andreForeldreNivå > søkernivå || barnNivå > søkernivå) {
            throwException()
        }
    }

    fun adresseNivå(adressebeskyttelseGradering: AdressebeskyttelseGradering?): Int {
        return when (adressebeskyttelseGradering) {
            null -> 0
            AdressebeskyttelseGradering.UGRADERT -> 0
            AdressebeskyttelseGradering.FORTROLIG -> 1
            AdressebeskyttelseGradering.STRENGT_FORTROLIG -> 2
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND -> 2
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
        return aktuelleBarn.map { it.value.forelderBarnRelasjon }
            .flatten()
            .filter { it.relatertPersonsIdent != søkersPersonIdent && it.relatertPersonsRolle != Familierelasjonsrolle.BARN }
            .map { it.relatertPersonsIdent }
            .distinct()
            .let { unsecurePdlStsClient.hentAndreForeldre(it) }
    }

    fun erILive(pdlBarn: PdlBarn) =
            pdlBarn.dødsfall.firstOrNull()?.dødsdato == null


    fun erIAktuellAlder(fødselsdato: LocalDate?): Boolean {
        if (fødselsdato == null) {
            // Vi vet ikke hva alder er, så vi filtrerer ikke bort denne
            return true
        }
        val alder = Period.between(fødselsdato, LocalDate.now())
        val alderIÅr = alder.years
        return alderIÅr <= regelverkConfig.alder.maks
    }

}



