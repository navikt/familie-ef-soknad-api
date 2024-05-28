package no.nav.familie.ef.søknad.person

import no.nav.familie.ef.søknad.infrastruktur.config.RegelverkConfig
import no.nav.familie.ef.søknad.infrastruktur.exception.ApiFeil
import no.nav.familie.ef.søknad.person.domain.Søkerinfo
import no.nav.familie.ef.søknad.person.dto.AdressebeskyttelseGradering
import no.nav.familie.ef.søknad.person.dto.Familierelasjonsrolle
import no.nav.familie.ef.søknad.person.dto.PdlAnnenForelder
import no.nav.familie.ef.søknad.person.dto.PdlBarn
import no.nav.familie.ef.søknad.person.dto.PdlSøker
import no.nav.familie.ef.søknad.person.dto.visningsnavn
import no.nav.familie.ef.søknad.person.mapper.SøkerinfoMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
internal class OppslagServiceServiceImpl(
    private val pdlClient: PdlClient,
    private val pdlApp2AppClient: PdlApp2AppClient,
    private val regelverkConfig: RegelverkConfig,
    private val søkerinfoMapper: SøkerinfoMapper,
) : OppslagService {
    override fun hentSøkerinfo(): Søkerinfo {
        val søkersPersonIdent = EksternBrukerUtils.hentFnrFraToken()
        val pdlSøker = pdlClient.hentSøker(søkersPersonIdent)
        val barnIdentifikatorer =
            pdlSøker.forelderBarnRelasjon
                .filter { it.relatertPersonsRolle == Familierelasjonsrolle.BARN }
                .mapNotNull { it.relatertPersonsIdent }
        val pdlBarn = pdlApp2AppClient.hentBarn(barnIdentifikatorer)
        val aktuelleBarn =
            pdlBarn
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
        andreForeldre: Map<String, PdlAnnenForelder>,
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
        søkersPersonIdent: String,
    ): Map<String, PdlAnnenForelder> {
        return aktuelleBarn.map { it.value.forelderBarnRelasjon }
            .flatten()
            .filter { it.relatertPersonsIdent != søkersPersonIdent && it.relatertPersonsRolle != Familierelasjonsrolle.BARN }
            .mapNotNull { it.relatertPersonsIdent }
            .distinct()
            .let { pdlApp2AppClient.hentAndreForeldre(it) }
    }

    fun erILive(pdlBarn: PdlBarn) = pdlBarn.dødsfall.firstOrNull()?.dødsdato == null

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
